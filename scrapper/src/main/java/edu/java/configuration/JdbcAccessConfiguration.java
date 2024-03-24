package edu.java.configuration;

import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcGitHubRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
@RequiredArgsConstructor
public class JdbcAccessConfiguration {

    private final JdbcMappersConfiguration jdbcMappersConfiguration;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcLinkRepository jdbcLinkRepository() {
        return new JdbcLinkRepository(jdbcTemplate, jdbcMappersConfiguration);
    }

    @Bean
    public JdbcChatRepository jdbcChatRepository() {
        return new JdbcChatRepository(jdbcTemplate, jdbcMappersConfiguration);
    }

    @Bean
    public JdbcGitHubRepository jdbcGitHubRepository() {
        return new JdbcGitHubRepository(jdbcTemplate, jdbcMappersConfiguration);
    }
}
