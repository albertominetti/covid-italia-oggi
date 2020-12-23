package it.minetti;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class EntryPointController {

    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @RequestMapping(value = {
            "{_:^(?!index\\.html|api|covid-19.png|swagger-ui|actuator).*$}"
    })
    public String forward() {
        log.info("URL entered directly into the Browser, so we need to redirect...");
        return "forward:/";
    }

}