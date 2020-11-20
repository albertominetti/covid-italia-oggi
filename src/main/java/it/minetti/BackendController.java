package it.minetti;

import it.minetti.CovidGraphsGenerator.GraphResult;
import it.minetti.graphs.GraphsService;
import it.minetti.graphs.GraphsService.GraphsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class BackendController {

    @Autowired
    private CovidGraphsGenerator generator;
    @Autowired
    private GraphsService graphsService;

    @GetMapping(value = "/")
    public ModelAndView method() {
        return new ModelAndView("redirect:/view-graphs");
    }

    @GetMapping(value = "/run")
    public GraphResult run() throws IOException {
        return generator.createLatestNationalGraphs();
    }

    @GetMapping(value = "/run-regions")
    public GraphResult runRegions() throws IOException {
        return generator.createLatestRegionalGraphs();
    }

    @GetMapping(value = "/covid-graphs")
    public GraphsHolder graphs() throws IOException {
        return graphsService.retrieveLatestGraphsRes();
    }

}