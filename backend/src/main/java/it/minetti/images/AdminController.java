package it.minetti.images;

import it.minetti.images.r.CovidGraphsGenerator;
import it.minetti.images.r.CovidGraphsGenerator.GraphsGenerationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@RequestMapping(value = "/admin")
public class AdminController {

    private final CovidGraphsGenerator generator;

    public AdminController(CovidGraphsGenerator generator) {
        this.generator = generator;
    }

    @GetMapping(value = "/generate-images")
    public GraphsGenerationResult run() throws IOException {
        return generator.createLatestGraphs();
    }

    @GetMapping(value = "/generate-national-images")
    public GraphsGenerationResult runNational() throws IOException {
        return generator.createLatestNationalGraphs();
    }

    @GetMapping(value = "/generate-regional-images")
    public GraphsGenerationResult runRegional() throws IOException {
        return generator.createLatestRegionalGraphs();
    }


}