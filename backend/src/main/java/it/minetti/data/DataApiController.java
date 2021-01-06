package it.minetti.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class DataApiController {

    private final DataRetrieverService service;

    public DataApiController(DataRetrieverService service) {
        this.service = service;
    }

    @GetMapping(value = "/data/national")
    public DataModel dataApi() {
        log.info("Someone requested the latest national data...");
        long start = System.currentTimeMillis();

        DataModel data = service.retrieveNationalData();

        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.debug("The retrieval of the last national data took: {}ms", totalTime);
        return data;
    }

    @GetMapping(value = "/data/regions/{regionCode}")
    public DataModel dataApi(@PathVariable String regionCode) {
        log.info("Someone requested the latest regional data for {}...", regionCode);
        long start = System.currentTimeMillis();

        DataModel data = service.retrieveRegionalData(regionCode);

        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.debug("The retrieval of the last regional data took: {}ms", totalTime);
        return data;
    }

}