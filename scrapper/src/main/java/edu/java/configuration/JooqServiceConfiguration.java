package edu.java.configuration;

import edu.java.clients.github.GitHubClient;
import edu.java.links.parser.LinkParser;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqGitHubRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.services.jooq.JooqChatService;
import edu.java.services.jooq.JooqLinkService;
import edu.java.services.jooq.JooqLinkTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqServiceConfiguration {
    private final LinkParser linkParser;
    private final GitHubClient gitHubClient;
    private final JooqGitHubRepository jooqGitHubRepository;
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqChatRepository jooqChatRepository;

    @Bean
    public JooqLinkService jooqLinkService() {
        return new JooqLinkService(jooqLinkRepository);
    }

    @Bean
    public JooqChatService jooqChatService() {
        return new JooqChatService(jooqChatRepository);
    }

    @Bean
    public JooqLinkTypeService jooqLinkTypeService() {
        return new JooqLinkTypeService(linkParser, gitHubClient, jooqGitHubRepository);
    }
}
