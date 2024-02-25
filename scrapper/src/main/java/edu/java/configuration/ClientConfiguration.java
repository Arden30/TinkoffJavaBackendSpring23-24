package edu.java.configuration;

import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.stackoverflow.StackOverFlowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClientImpl gitHubClient() {
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverFlowClientImpl stackOverFlowClient() {
        return new StackOverFlowClientImpl();
    }
}
