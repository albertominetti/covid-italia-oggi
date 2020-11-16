package it.minetti;

import it.minetti.CovidGraphsGenerator.GraphResult;
import it.minetti.ViewService.GraphsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class BackendController {

    @Autowired
    private CovidGraphsGenerator generator;
    @Autowired
    private ViewService viewService;

    @GetMapping(value = "/")
    public ModelAndView method() {
        return new ModelAndView("redirect:/actuator/health/");
    }

    @GetMapping(value = "/run")
    public GraphResult run() throws IOException {
        return generator.createLatestGraphs();
    }

    @GetMapping(value = "/covid-graphs")
    public GraphsHolder graphs() throws IOException {
        return viewService.retrieveLatestGraphs();
    }

}