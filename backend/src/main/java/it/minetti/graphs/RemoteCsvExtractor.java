package it.minetti.graphs;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RemoteCsvExtractor {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pcm-dpc.url}")
    private String url;

    public LocalDate retrieveLastDayInCsv() {
        String rawCsv = restTemplate.getForObject(url, String.class);
        if (rawCsv == null) {
            throw new IllegalArgumentException("Cannot retrieve data from source.");
        }

        List<String[]> lines;
        try (Reader inputReader = new StringReader(rawCsv)) {
            CsvParserSettings settings = new CsvParserSettings();
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

        if (lastLine.length <= 1) {
            throw new IllegalArgumentException("Empty data from the csv.");
        }

        String lastTimeFromCsv = lastLine[0];
        return LocalDateTime.parse(lastTimeFromCsv).toLocalDate();
    }
}
