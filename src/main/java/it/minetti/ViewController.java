package it.minetti;

import it.minetti.ViewService.GraphsHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.minetti.config.GraphsResourceWebConfiguration.GRAPHS_DIR;

@Controller
public class ViewController {

    @Autowired
    private ViewService viewService;

    @GetMapping("/view-graphs")
    public String graphs(Model model) throws IOException {
        GraphsHolder graphsHolder = viewService.retrieveLatestGraphs();

        model.addAttribute("day", graphsHolder.getDay());
        model.addAttribute("graphs", graphsHolder.getGraphs());

        return "view";
    }
}