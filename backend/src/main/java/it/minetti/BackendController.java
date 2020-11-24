package it.minetti;

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
    private LocalGraphsService localGraphsService;

    @GetMapping(value = "/")
    public ModelAndView method() {
        return new ModelAndView("redirect:/view-graphs");
    }

    @GetMapping(value = "/api/graphs")
    public GraphsHolder graphsApi() throws IOException {
        return localGraphsService.retrieveLatestGraphsRes();
    }

}