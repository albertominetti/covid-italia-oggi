package it.minetti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.notExists;

@Configuration
public class GraphsResourceWebConfiguration implements WebMvcConfigurer {

    public static final String GRAPHS_DIR_NAME = "graphs";
    public static final Path GRAPHS_DIR = Paths.get(GRAPHS_DIR_NAME);

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + GRAPHS_DIR_NAME + "/**").addResourceLocations("file:" + GRAPHS_DIR_NAME + "/");
    }

    @PostConstruct
    public void setUp() throws IOException {
        if (notExists(GRAPHS_DIR)) {
            createDirectory(GRAPHS_DIR);
        }
    }
}