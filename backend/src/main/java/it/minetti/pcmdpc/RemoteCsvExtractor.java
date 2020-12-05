package it.minetti.pcmdpc;

import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.conversions.Conversion;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class RemoteCsvExtractor {

    private final RestTemplate restTemplate;

    @Value("${pcm-dpc.url}") // TODO move in constructor
    private String url;

    public RemoteCsvExtractor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocalDate retrieveLastDayInCsv() {
        String rawCsv = restTemplate.getForObject(url, String.class);
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

    public List<CsvRow> retrieveLastData() {
        log.info("Retrieving latest national data...");
        String rawCsv = restTemplate.getForObject(url, String.class);
        if (rawCsv == null) {
            throw new IllegalArgumentException("Cannot retrieve data from source.");
        }

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

    @Data
    public static class CsvRow {

        @Parsed(field = "data")
        @Convert(conversionClass = LocalDateFormatter.class, args = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDate date;

        @Parsed(field = "deceduti")
        private int totalDeceased;

        @Parsed(field = "terapia_intensiva")
        private int inIntensiveCare;

        @Parsed(field = "nuovi_positivi")
        private int newPositives;

        @Parsed(field = "tamponi")
        private int totalTests;
    }

    public static class LocalDateFormatter implements Conversion<String, LocalDate> {

        private final DateTimeFormatter formatter;

        public LocalDateFormatter(String... args) {
            String pattern = "yyyy-MM-dd";
            if (args.length > 0) {
                pattern = args[0];
            }
            this.formatter = DateTimeFormatter.ofPattern(pattern);
        }

        @Override
        public LocalDate execute(String input) {
            return LocalDate.parse(input, formatter);
        }

        @Override
        public String revert(LocalDate input) {
            return formatter.format(input);
        }
    }
}
