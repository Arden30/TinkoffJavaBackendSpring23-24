package edu.java.configuration;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.clients.stackoverflow.StackOverFlowClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {
    private final GitHubConfig gitHubConfig;
    private final StackOverFlowConfig stackOverflowConfig;
    private final BotConfig botConfig;

    @Bean
    public BotWebClient botWebClient() {
        return new BotWebClient(botConfig);
    }

    @Bean
    public GitHubClient gitHubClient() {
        return new GitHubClientImpl(gitHubConfig);
    }

    @Bean
    public StackOverFlowClient stackOverFlowClient() {
        return new StackOverFlowClientImpl(stackOverflowConfig);
    }
}
