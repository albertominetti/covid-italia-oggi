package it.minetti.data;

import it.minetti.pcmdpc.RemoteCsvExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class AdminDataController {

    private final RemoteCsvExtractor extractor;

    public AdminDataController(RemoteCsvExtractor extractor) {
        this.extractor = extractor;
    }
    
    @GetMapping(value = "/data-evict")
    public void evictApiIfNew() {
        extractor.evictAllData();
        log.info("Data eviction completed.");
    }

}