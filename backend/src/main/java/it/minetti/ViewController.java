package it.minetti;

import it.minetti.images.LocalGraphsService;
import it.minetti.images.LocalGraphsService.GraphsHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
@Deprecated
public class ViewController {

    @Autowired
    private LocalGraphsService localGraphsService;

    @GetMapping("/view-graphs")
    public String graphs(Model model) throws IOException {
        log.debug("A request for the graphs.");
        GraphsHolder graphsHolder = localGraphsService.retrieveLatestGraphsRes();

        model.addAttribute("day", graphsHolder.getDay());
        model.addAttribute("graphs", graphsHolder.getUrls());

        return "view";
    }
}