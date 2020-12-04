package it.minetti.images;

import it.minetti.images.LocalGraphsService;
import it.minetti.images.LocalGraphsService.GraphsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api")
public class ImagesApiController {

    private final LocalGraphsService localGraphsService;

    public ImagesApiController(LocalGraphsService localGraphsService) {
        this.localGraphsService = localGraphsService;
    }

    @GetMapping(value = "/images")
    public GraphsHolder graphsApi() throws IOException {
        return localGraphsService.retrieveLatestGraphsRes();
    }

}