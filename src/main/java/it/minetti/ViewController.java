package it.minetti;

import it.minetti.graphs.LocalGraphsService;
import it.minetti.graphs.LocalGraphsService.GraphsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ViewController {

    @Autowired
    private LocalGraphsService localGraphsService;

    @GetMapping("/view-graphs")
    public String graphs(Model model) throws IOException {
        GraphsHolder graphsHolder = localGraphsService.retrieveLatestGraphsRes();

        model.addAttribute("day", graphsHolder.getDay());
        model.addAttribute("graphs", graphsHolder.getUrls());

        return "view";
    }
}