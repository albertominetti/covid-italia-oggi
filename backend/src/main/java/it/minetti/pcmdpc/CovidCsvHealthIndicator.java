package it.minetti.pcmdpc;

import it.minetti.config.PcmDpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CovidCsvHealthIndicator implements HealthIndicator {

    private final RestTemplate template;
    private final String url;

    public CovidCsvHealthIndicator(RestTemplate template, PcmDpcProperties properties) {
        this.template = template;
        this.url = properties.getNationalCsvUrl();
    }

    @Override
    public Health health() {

        try {
            template.headForHeaders(url);
            return Health.up()
                    .build();

        } catch (Exception e) {
            log.warn("Failed to connect to: {}", url);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

}