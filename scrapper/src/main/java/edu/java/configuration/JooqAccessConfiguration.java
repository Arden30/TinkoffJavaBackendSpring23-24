package edu.java.configuration;

import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqGitHubRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqAccessConfiguration {
    private final DSLContext dsl;

    @Bean
    public JooqLinkRepository jooqLinkRepository() {
        return new JooqLinkRepository(dsl);
    }

    @Bean
    public JooqChatRepository jooqChatRepository() {
        return new JooqChatRepository(dsl);
    }

    @Bean
    public JooqGitHubRepository jooqGitHubRepository() {
        return new JooqGitHubRepository(dsl);
    }
}
