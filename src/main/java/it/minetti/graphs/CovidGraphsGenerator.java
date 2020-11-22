package it.minetti.graphs;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;

@Slf4j
@Service
public class CovidGraphsGenerator {

    public static final String PLOT_NATIONAL = "/plot_national.R";
    public static final String PLOT_REGIONS = "/plot_regions.R";

    @Autowired
    private LocalGraphsService localGraphsService;

    @Autowired
    private RemoteCsvExtractor remoteCsvExtractor;


    @Scheduled(cron = "0 55 17 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 00 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 10 18 * * ?", zone = "Europe/Rome")
    @Scheduled(cron = "0 15 19 * * ?", zone = "Europe/Rome")
    public GraphResult runIfNewData() throws IOException {
        Optional<LocalDate> optionalLastDayOfCalculation = localGraphsService.retrieveLatestCalculatedDay();
        if (optionalLastDayOfCalculation.isEmpty()) {
            log.warn("No graph has ever been calculated since now, seems it is the first time.");
            return createLatestGraphs();
        } else {
            LocalDate lastDayOfCalculation = optionalLastDayOfCalculation.get();
            if (lastDayOfCalculation.isEqual(LocalDate.now())) {
                // the data for the day are published within the day
                log.warn("No needs to create the new graphs since they are updated to {}.", lastDayOfCalculation);
                return new GraphResult("ok", Duration.ZERO, lastDayOfCalculation.toString());
            }

            LocalDate lastDayFromCsv = remoteCsvExtractor.retrieveLastDayInCsv();
            if (lastDayOfCalculation.isBefore(lastDayFromCsv)) {
                log.info("There are new data for graphs calculation, running the extraction right now.");
                return createLatestGraphs();
            } else {
                log.warn("There are no data till now for the current day, wait for it and run manually or wait for the next run.");
                return new GraphResult("no", Duration.ZERO, null);
            }

        }
    }

    private GraphResult createLatestGraphs() throws IOException {
        GraphResult nationalGraphs = createLatestNationalGraphs();
        GraphResult regionalGraphs = createLatestRegionalGraphs();

        Duration totalTime = nationalGraphs.getTime().plus(regionalGraphs.getTime());

        if (!StringUtils.equals(nationalGraphs.getLastDay(), regionalGraphs.getLastDay())) {
            // should never happen, but better to log something if it happens
            log.warn("The data for regions and nation have not been update together as expected");
        }

        log.info("New graphs have been created with national and regional data.");
        return new GraphResult("ok", totalTime, nationalGraphs.getLastDay());
    }

    public GraphResult createLatestNationalGraphs() throws IOException {
        return this.createGraphs(PLOT_NATIONAL);
    }

    public GraphResult createLatestRegionalGraphs() throws IOException {
        return this.createGraphs(PLOT_REGIONS);
    }

    private GraphResult createGraphs(String rScript) throws IOException {
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