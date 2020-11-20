package it.minetti;

import it.minetti.graphs.GraphsService;
import it.minetti.graphs.GraphsService.GraphsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ViewController {

    @Autowired
    private GraphsService graphsService;

    @GetMapping("/view-graphs")
    public String graphs(Model model) throws IOException {
        GraphsHolder graphsHolder = graphsService.retrieveLatestGraphsRes();

        model.addAttribute("day", graphsHolder.getDay());
        model.addAttribute("graphs", graphsHolder.getUrls());

        return "view";
    }
}