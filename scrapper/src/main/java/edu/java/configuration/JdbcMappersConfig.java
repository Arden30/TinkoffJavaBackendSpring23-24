package edu.java.configuration;

import edu.java.model.Chat;
import edu.java.model.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Configuration
@RequiredArgsConstructor
public class JdbcMappersConfig {
    @Bean
    public BeanPropertyRowMapper<Link> linkMapper() {
        return new BeanPropertyRowMapper<>(Link.class);
    }

    @Bean
    public BeanPropertyRowMapper<Chat> chatMapper() {
        return new BeanPropertyRowMapper<>(Chat.class);
    }
}
