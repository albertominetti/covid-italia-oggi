package it.minetti;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    public GraphResult createLatestNationalGraphs() throws IOException {
        return this.createGraphs(PLOT_NATIONAL);
    }

    public GraphResult createLatestRegionalGraphs() throws IOException {
        return this.createGraphs(PLOT_REGIONS);
    }

    private GraphResult createGraphs(String rScript) throws IOException {
        log.info("Running");
        long start = System.currentTimeMillis();
        String rSource = resourceToString(rScript, UTF_8);
        RCode code = RCode.create();
        code.addRCode(rSource);
        RCaller caller = RCaller.create(code, RCallerOptions.create());
        caller.runAndReturnResult("lastDay");
        String lastDay = caller.getParser().getAsStringArray("lastDay")[0];
        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.info("Graph creation took: {}ms", totalTime);
        return new GraphResult("ok", Duration.ofMillis(totalTime), lastDay);
    }

    @Data
    @AllArgsConstructor
    public static class GraphResult {
        private String status = "ok";
        private Duration time;
        private String lastDay;
    }
}