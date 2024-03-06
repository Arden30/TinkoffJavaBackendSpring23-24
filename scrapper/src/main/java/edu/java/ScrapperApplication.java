package edu.java;

import edu.java.configuration.ApplicationConfig;
import edu.java.configuration.BotConfig;
import edu.java.configuration.GitHubConfig;
import edu.java.configuration.StackOverFlowConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationConfig.class,
    GitHubConfig.class,
    StackOverFlowConfig.class,
    BotConfig.class})
public class ScrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperApplication.class, args);
    }
}
