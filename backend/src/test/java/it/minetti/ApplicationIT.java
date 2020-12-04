package it.minetti;

import it.minetti.images.LocalGraphsService;
import it.minetti.images.LocalGraphsService.GraphsHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ApplicationIT.SmokeConfig.class)
class ApplicationIT {

    @Autowired
    TestRestTemplate template;

    @LocalServerPort
    private int port;

    @MockBean
    LocalGraphsService localGraphsService;

    @Test
    void contextLoad() throws IOException {
        when(localGraphsService.retrieveLatestGraphsRes()).thenReturn(new GraphsHolder());

        ResponseEntity<String> entity = template.getForEntity("http://localhost:{port}/", String.class, port);
        assertThat(entity, is(not(nullValue())));
        assertThat(entity.getStatusCode(), is(in(newHashSet(OK, FOUND))));
    }

    @Test
    void health() throws IOException {
        when(localGraphsService.retrieveLatestGraphsRes()).thenReturn(new GraphsHolder());

        ResponseEntity<String> entity = template.getForEntity("http://localhost:{port}/actuator/health", String.class, port);
        assertThat(entity, is(not(nullValue())));
        assertThat(entity.getStatusCode(), is(in(newHashSet(OK, FOUND))));
    }

    @TestConfiguration
    public static class SmokeConfig {
        @Bean
        public TestRestTemplate testRestTemplate() {
            return new TestRestTemplate();
        }
    }
}