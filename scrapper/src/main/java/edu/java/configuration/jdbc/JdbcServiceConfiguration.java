package edu.java.configuration.jdbc;

import edu.java.links.listener.LinkListener;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.services.jdbc.JdbcChatService;
import edu.java.services.jdbc.JdbcLinkService;
import java.util.List;
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
    private final List<LinkListener> linkListeners;

    @Bean
    public JdbcLinkService jdbcLinkService() {
        return
            new JdbcLinkService(linkListeners, jdbcLinkRepository);
    }

    @Bean
    public JdbcChatService jdbcChatService() {
        return new JdbcChatService(jdbcChatRepository);
    }
}
