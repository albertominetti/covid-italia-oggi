package it.minetti.graphs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class CovidCsvHealthIndicator implements HealthIndicator {

    @Value("${pcm-dpc.url}")
    private String url;

    @Autowired
    RestTemplate template;

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