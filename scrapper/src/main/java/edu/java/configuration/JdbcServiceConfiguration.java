package edu.java.configuration;

import edu.java.clients.github.GitHubClient;
import edu.java.links.parser.LinkParser;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcGitHubRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.services.jdbc.JdbcChatService;
import edu.java.services.jdbc.JdbcLinkService;
import edu.java.services.jdbc.JdbcLinkTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcServiceConfiguration {
    private final JdbcLinkRepository jdbcLinkRepository;
    private final JdbcChatRepository jdbcChatRepository;

    private final JdbcGitHubRepository jdbcGitHubRepository;

    private final LinkParser linkParser;
    private final GitHubClient gitHubClient;

    @Bean
    public JdbcLinkService jdbcLinkService() {
        return new JdbcLinkService(jdbcLinkRepository);
    }

    @Bean
    public JdbcChatService jdbcChatService() {
        return new JdbcChatService(jdbcChatRepository);
    }

    @Bean
    public JdbcLinkTypeService jdbcLinkTypeService() {
        return new JdbcLinkTypeService(linkParser, gitHubClient, jdbcGitHubRepository);
    }
}
