package it.minetti.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("pcm-dpc")
public class PcmDpcProperties {
    private String nationalCsvUrl;
    private String regionalCsvUrl;
}
