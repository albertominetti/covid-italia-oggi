package it.minetti.pcmdpc;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import it.minetti.config.PcmDpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class RemoteCsvExtractor {

    private static final String NATIONAL_DATA = "national-data";
    private static final String REGIONAL_DATA = "regional-data";

    private final RestTemplate restTemplate;
    private final PcmDpcProperties properties;

    public RemoteCsvExtractor(RestTemplate restTemplate, PcmDpcProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public LocalDate retrieveLastDayInCsv() {
        String rawCsv = restTemplate.getForObject(properties.getNationalCsvUrl(), String.class);
        if (rawCsv == null) {
            throw new IllegalArgumentException("Cannot retrieve data from source.");
        }

        List<String[]> lines;
        try (Reader inputReader = new StringReader(rawCsv)) {
            CsvParserSettings settings = new CsvParserSettings();
            settings.selectFields("data");
            settings.setLineSeparatorDetectionEnabled(true);
            CsvParser parser = new CsvParser(settings);
            // no need to parse into objects, we only need the last row
            lines = parser.parseAll(inputReader);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

        String[] lastLine = lines.stream()
                .reduce((first, second) -> second).orElseThrow(
                        () -> new IllegalArgumentException("Impossible to find last line in the csv provided.")
                );

        if (lastLine.length < 1) {
            throw new IllegalArgumentException("Empty data from the csv.");
        }

        String lastTimeFromCsv = lastLine[0];
        return LocalDateTime.parse(lastTimeFromCsv).toLocalDate();
    }


    @Cacheable(value = NATIONAL_DATA)
    public List<CsvRow> retrieveLastNationalData() {
        log.info("Retrieving latest national data...");
        String rawCsv = restTemplate.getForObject(properties.getNationalCsvUrl(), String.class);
        if (rawCsv == null) {
            throw new IllegalArgumentException("Cannot retrieve data from source.");
        }

        return extractCsvRows(rawCsv);
    }

    @Cacheable(value = REGIONAL_DATA)
    public List<CsvRow> retrieveLastRegionalData() {
        log.info("Retrieving latest national data...");
        String rawCsv = restTemplate.getForObject(properties.getRegionalCsvUrl(), String.class);
        if (rawCsv == null) {
            throw new IllegalArgumentException("Cannot retrieve data from source.");
        }

        return extractCsvRows(rawCsv);
    }

    private List<CsvRow> extractCsvRows(String rawCsv) {
        try (Reader inputReader = new StringReader(rawCsv)) {
            BeanListProcessor<CsvRow> rowProcessor = new BeanListProcessor<>(CsvRow.class);

            CsvParserSettings settings = new CsvParserSettings();
            settings.setProcessor(rowProcessor);
            settings.setLineSeparatorDetectionEnabled(true);
            settings.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(settings);
            // no need to parse into objects, we only need the last row
            parser.parse(inputReader);

            log.debug("Latest data retrieved.");
            return rowProcessor.getBeans();
        } catch (IOException e) {
            log.error("Something went wrong when retrieving the latest data", e);
            throw new IllegalArgumentException(e);
        }
    }

    @CacheEvict(value = {NATIONAL_DATA, REGIONAL_DATA}, allEntries = true)
    public void evictAllData() {
    }

}
