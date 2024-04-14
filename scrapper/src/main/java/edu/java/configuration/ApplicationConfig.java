package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,

    @NotNull
    AccessType databaseAccessType,

    @NotNull
    KafkaProducerSettings kafkaProducerSettings,

    @NotNull
    KafkaUpdateTopic kafkaUpdateTopic,

    Boolean useQueue
) {
    @Bean
    public Duration forceCheckDelay() {
        return scheduler.forceCheckDelay();
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    public enum AccessType {
        JDBC, JPA,
        JOOQ
    }

    public record Scheduler(boolean enable, @NotNull Duration interval,
                            @NotNull Duration forceCheckDelay) {
    }

    public record KafkaProducerSettings(@NotNull String bootstrapServers,
                                        String clientId,
                                        Duration deliveryTimeout,
                                        Integer lingerMs,
                                        Integer batchSize,
                                        Boolean enableIdempotence) {
    }

    public record KafkaUpdateTopic(String name,
                                  Integer partitions,
                                  Integer replicas) {
    }
}
