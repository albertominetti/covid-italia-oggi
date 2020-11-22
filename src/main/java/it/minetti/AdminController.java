package it.minetti;

import it.minetti.graphs.CovidGraphsGenerator;
import it.minetti.graphs.CovidGraphsGenerator.GraphResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private CovidGraphsGenerator generator;

    @GetMapping(value = "/run")
    public GraphResult run() throws IOException {
        return generator.createLatestGraphs();
    }

    @GetMapping(value = "/run-national")
    public GraphResult runNational() throws IOException {
        return generator.createLatestNationalGraphs();
    }

    @GetMapping(value = "/run-regional")
    public GraphResult runRegional() throws IOException {
        return generator.createLatestRegionalGraphs();
    }


}