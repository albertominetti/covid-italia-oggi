package it.minetti;

import it.minetti.graphs.GraphsService.GraphsHolder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = GraphsServiceIT.SmokeConfig.class)
class GraphsServiceIT {

    @Autowired
    TestRestTemplate template;

    @LocalServerPort
    private int port;

    @BeforeAll
    @AfterAll
    static void cleanGraphDirectory() throws IOException {
        FileUtils.deleteDirectory(Paths.get("graphs").toFile());
    }

    @Test
    void graphsLoad() throws IOException {
        LocalDate today = LocalDate.now();
        Path todayGraphFolder = Paths.get("graphs", today.toString());
        Files.createDirectories(todayGraphFolder);
        byte[] sampleImageBytes = toByteArray(this.getClass().getClassLoader().getResourceAsStream("sample.png"));
        Files.write(todayGraphFolder.resolve("sample1.png"), sampleImageBytes, TRUNCATE_EXISTING, CREATE);
        Files.write(todayGraphFolder.resolve("sample2.png"), sampleImageBytes, TRUNCATE_EXISTING, CREATE);

        ResponseEntity<GraphsHolder> entity = template.getForEntity("http://localhost:{port}/covid-graphs", GraphsHolder.class, port);
        assertThat(entity, is(not(nullValue())));
        assertThat(entity.getStatusCode(), is(OK));
        GraphsHolder body = entity.getBody();
        assertThat(body, is(not(nullValue())));
        assertThat(body.getDay(), is(today));
        assertThat(body.getUrls(), is(not(empty())));
        assertThat(body.getUrls(), containsInAnyOrder(
                format("http://localhost:%d/graphs/%s/sample1.png", port, today),
                format("http://localhost:%d/graphs/%s/sample2.png", port, today)
        ));
    }

    @TestConfiguration
    public static class SmokeConfig {
        @Bean
        public TestRestTemplate testRestTemplate() {
            return new TestRestTemplate();
        }
    }
}