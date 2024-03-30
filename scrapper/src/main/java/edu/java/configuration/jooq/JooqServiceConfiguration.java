package edu.java.configuration.jooq;

import edu.java.links.listener.LinkListener;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.services.jooq.JooqChatService;
import edu.java.services.jooq.JooqLinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
@RequiredArgsConstructor
public class JooqServiceConfiguration {
    private final List<LinkListener> linkListeners;
    private final JooqLinkRepository jooqLinkRepository;
    private final JooqChatRepository jooqChatRepository;

    @Bean
    public JooqLinkService jooqLinkService() {
        return new JooqLinkService(jooqLinkRepository, linkListeners);
    }

    @Bean
    public JooqChatService jooqChatService() {
        return new JooqChatService(jooqChatRepository);
    }
}
