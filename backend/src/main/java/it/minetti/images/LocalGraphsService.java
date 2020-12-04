package it.minetti.images;

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
import java.util.Optional;
import java.util.stream.Collectors;

import static it.minetti.images.GraphsResourceWebConfiguration.GRAPHS_DIR_NAME;
import static it.minetti.images.GraphsResourceWebConfiguration.GRAPHS_DIR_URI;
import static java.util.Collections.emptyList;

@Slf4j
@Service
public class LocalGraphsService {

    private final ResourcePatternResolver resolver =
            new PathMatchingResourcePatternResolver(LocalGraphsService.class.getClassLoader());

    public Optional<LocalDate> retrieveLatestCalculatedDay() throws IOException {
        return retrieveLatestDayDirectory().map(LocalDate::parse);
    }

    public GraphsHolder retrieveLatestGraphsRes() throws IOException {
        Optional<String> latestDayDirectory = retrieveLatestDayDirectory();
        if (latestDayDirectory.isEmpty()) {
            throw new IllegalArgumentException("No graph is still present");
        }
        return retrieveGraphsForTheDay(latestDayDirectory.get());
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

    public Optional<String> retrieveLatestDayDirectory() throws IOException {
        Resource[] dayResources = resolver.getResources(GRAPHS_DIR_URI + "/*");
        return Arrays.stream(dayResources)
                .map(Resource::getFilename)
                .filter(StringUtils::isNotBlank)
                .max(String::compareTo);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GraphsHolder {
        private LocalDate day;
        private List<String> urls = emptyList();
    }
}
