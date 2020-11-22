package it.minetti;

import it.minetti.graphs.CovidGraphsGenerator;
import it.minetti.graphs.CovidGraphsGenerator.GraphResult;
import it.minetti.graphs.LocalGraphsService;
import it.minetti.graphs.LocalGraphsService.GraphsHolder;
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
    private LocalGraphsService localGraphsService;

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
        return localGraphsService.retrieveLatestGraphsRes();
    }

}