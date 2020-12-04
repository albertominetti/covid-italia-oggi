package it.minetti.data;

import it.minetti.pcmdpc.RemoteCsvExtractor;
import it.minetti.pcmdpc.RemoteCsvExtractor.CsvRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class DataApiController {

    private final RemoteCsvExtractor remoteCsvExtractor;
    private final CsvDataMapper mapper;

    public DataApiController(RemoteCsvExtractor remoteCsvExtractor, CsvDataMapper mapper) {
        this.remoteCsvExtractor = remoteCsvExtractor;
        this.mapper = mapper;
    }

    @GetMapping(value = "/data")
    public DataModel graphsApi() {
        log.info("Someone requested the latest data...");
        long start = System.currentTimeMillis();
        List<CsvRow> csvRows = remoteCsvExtractor.retrieveLastData();
        // TODO cache it
        DataModel data = mapper.map(csvRows);

        long stop = System.currentTimeMillis();
        long totalTime = stop - start;
        log.info("The retrieval of the last data took: {}ms", totalTime);
        return data;
    }

}