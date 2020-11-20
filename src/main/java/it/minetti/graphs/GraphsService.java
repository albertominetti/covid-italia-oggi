package it.minetti.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.minetti.graphs.GraphsResourceWebConfiguration.GRAPHS_DIR_NAME;
import static it.minetti.graphs.GraphsResourceWebConfiguration.GRAPHS_DIR_URI;
import static java.util.Collections.emptyList;

@Slf4j
@Service
public class GraphsService {

    private final ResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver(GraphsService.class.getClassLoader());

    public GraphsHolder retrieveLatestGraphsRes() throws IOException {
        Resource[] dayResources = resolver.getResources(GRAPHS_DIR_URI + "/*");
        String lastDayDirectory = Arrays.stream(dayResources)
                .map(Resource::getFilename)
                .filter(StringUtils::isNotBlank)
                .max(String::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("No graph is still present"));
        return retrieveGraphsForTheDay(lastDayDirectory);
    }

    private GraphsHolder retrieveGraphsForTheDay(String lastDayDirectory) throws IOException {
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Resource[] lastDayGraphResources = resolver.getResources(GRAPHS_DIR_URI + "/" + lastDayDirectory + "/*");
        List<String> lastDayGraphs = Arrays.stream(lastDayGraphResources)
                .map(Resource::getFilename)
                .filter(f -> StringUtils.endsWith(f, ".png"))
                .sorted()
                .map(z -> baseUrl + "/" + GRAPHS_DIR_NAME + "/" + lastDayDirectory + "/" + z)
                .collect(Collectors.toList());
        return new GraphsHolder(LocalDate.parse(lastDayDirectory), lastDayGraphs);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphsHolder {
        private LocalDate day;
        private List<String> urls = emptyList();
    }
}
