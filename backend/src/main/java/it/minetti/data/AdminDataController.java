package it.minetti.data;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
public class AdminDataController {

    private final DataRetrieverService dataRetrieverService;

    public AdminDataController(DataRetrieverService dataRetrieverService) {
        this.dataRetrieverService = dataRetrieverService;
    }

    @GetMapping(value = "/data-evict")
    public void evictApiIfNew() {
        dataRetrieverService.evictAllData();
    }

}