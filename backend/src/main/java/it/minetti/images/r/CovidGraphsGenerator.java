package it.minetti.images.r;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;

@Slf4j
@Service
public class CovidGraphsGenerator {

    public static final String PLOT_NATIONAL = "/plot_national.R";
    public static final String PLOT_REGIONS = "/plot_regions.R";


    public GraphsGenerationResult createLatestGraphs() throws IOException {
        GraphsGenerationResult nationalGraphs = createLatestNationalGraphs();
        GraphsGenerationResult regionalGraphs = createLatestRegionalGraphs();

        Duration totalTime = nationalGraphs.getTime().plus(regionalGraphs.getTime());

        if (!StringUtils.equals(nationalGraphs.getLastDay(), regionalGraphs.getLastDay())) {
            // should never happen, but better to log something if it happens
            log.warn("The data for regions and nation have not been update together as expected");
        }

        log.info("New graphs have been created with national and regional data.");
        return new GraphsGenerationResult("ok", totalTime, nationalGraphs.getLastDay());
    }

    public GraphsGenerationResult createLatestNationalGraphs() throws IOException {
        return this.createGraphs(PLOT_NATIONAL);
    }

    public GraphsGenerationResult createLatestRegionalGraphs() throws IOException {
        return this.createGraphs(PLOT_REGIONS);
    }

    private GraphsGenerationResult createGraphs(String rScript) throws IOException {
        log.info("Running {}", rScript);
        long start = System.currentTimeMillis();
        String rSource = resourceToString(rScript, UTF_8);
        RCode code = RCode.create();
        code.addRCode(rSource);
        RCaller caller = RCaller.create(code, RCallerOptions.create());
        caller.runAndReturnResult("lastDay");
        String lastDay = caller.getParser().getAsStringArray("lastDay")[0];
        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.info("Graphs creation took: {}ms", totalTime);
        return new GraphsGenerationResult("ok", Duration.ofMillis(totalTime), lastDay);
    }

    @Data
    @AllArgsConstructor
    public static class GraphsGenerationResult {
        private String status;
        private Duration time;
        private String lastDay;
    }
}