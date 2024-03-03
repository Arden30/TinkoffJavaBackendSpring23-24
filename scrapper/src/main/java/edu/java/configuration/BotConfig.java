package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "bot-client", ignoreUnknownFields = false)
public record BotConfig(
    @NotEmpty
    String baseUrl
) {
}
