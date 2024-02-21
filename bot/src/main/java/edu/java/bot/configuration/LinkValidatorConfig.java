package edu.java.bot.configuration;

import edu.java.bot.links.GitHubLink;
import edu.java.bot.links.LinkValidator;
import edu.java.bot.links.StackOverFlowLink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkValidatorConfig {
    @Bean
    public LinkValidator linkValidator() {
        return LinkValidator.createChain(
            new GitHubLink(),
            new StackOverFlowLink()
        );
    }
}
