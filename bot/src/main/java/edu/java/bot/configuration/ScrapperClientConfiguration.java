package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ScrapperClientConfiguration {
    private final ScrapperConfig scrapperConfig;

    @Bean
    public ScrapperWebClient botClient() {
        return new ScrapperWebClient(scrapperConfig);
    }
}
