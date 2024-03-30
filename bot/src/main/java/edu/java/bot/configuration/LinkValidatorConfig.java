package edu.java.bot.configuration;

import edu.java.parser.GitHubParser;
import edu.java.parser.LinkParser;
import edu.java.parser.StackOverFlowParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkValidatorConfig {
    @Bean
    public LinkParser linkParser() {
        return LinkParser.createChain(new GitHubParser(), new StackOverFlowParser());
    }
}
