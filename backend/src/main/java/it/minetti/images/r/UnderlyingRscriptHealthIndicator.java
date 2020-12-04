package it.minetti.images.r;

import com.github.rcaller.rstuff.RCaller;
import com.github.rcaller.rstuff.RCallerOptions;
import com.github.rcaller.rstuff.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnderlyingRscriptHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            long start = System.currentTimeMillis();
            RCode code = RCode.create();
            code.addRCode("customMean <- function(vector) { mean(vector) }");
            code.addIntArray("input", new int[]{10, 20, 30, 40});
            code.addRCode("result <- customMean(input)");
            RCaller caller = RCaller.create(code, RCallerOptions.create());

            caller.runAndReturnResult("result");
            double result = caller.getParser().getAsDoubleArray("result")[0];
            long stop = System.currentTimeMillis();
            long totalTime = stop - start;
            log.debug("R computation for health check took: {}ms", totalTime);

            return (result == 25.0) ? Health.up().build()
                    : Health.unknown()
                    .withDetail("error", "Wrong calculation for mean")
                    .build();

        } catch (Exception e) {
            log.warn("Failed to connect to R", e);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}