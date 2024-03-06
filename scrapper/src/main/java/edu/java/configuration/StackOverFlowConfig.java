package edu.java.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "stackoverflow-client", ignoreUnknownFields = false)
public record StackOverFlowConfig(
    @NotEmpty
    String baseUrl
) {
}
