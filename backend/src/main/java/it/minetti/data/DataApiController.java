package it.minetti.data;

import it.minetti.scheduled.DataSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class DataApiController {


    private final DataRetrieverService service;
    private final DataSchedulerService scheduler;

    public DataApiController(DataRetrieverService service, DataSchedulerService scheduler) {
        this.service = service;
        this.scheduler = scheduler;
    }

    @GetMapping(value = "/data")
    public DataModel graphsApi() {
        log.info("Someone requested the latest data...");
        long start = System.currentTimeMillis();

        DataModel data = service.retrieveNationalData();

        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.info("The retrieval of the last data took: {}ms", totalTime);
        return data;
    }

}