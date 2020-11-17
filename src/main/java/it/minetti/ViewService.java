package it.minetti;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.minetti.config.GraphsResourceWebConfiguration.GRAPHS_DIR;
import static java.util.Collections.emptyList;

@Service
public class ViewService {
    public GraphsHolder retrieveLatestGraphs() throws IOException {
        try (Stream<Path> x = Files.list(GRAPHS_DIR)) {
            Path lastDayDirectory = x.filter(Files::isDirectory)
                    .max(Comparator.comparing(p -> p.getFileName().toString()))
                    .orElseThrow(() -> new IllegalArgumentException("No graph is still present"));
            GraphsHolder graphsHolder = new GraphsHolder(LocalDate.parse(lastDayDirectory.getFileName().toString()));

            try (Stream<Path> y = Files.list(lastDayDirectory)) {
                List<String> graphs = y.filter(Files::isRegularFile)
                        .filter(f -> StringUtils.endsWith(f.getFileName().toString(), ".png"))
                        .map(Path::toString)
                        .collect(Collectors.toList());
                graphsHolder.setGraphs(graphs);
            }

            return graphsHolder;
        }
    }

    @Data
    public static class GraphsHolder {
        private final LocalDate day;

        // TODO Use hateoas
        private List<String> graphs = emptyList();
    }
}
