package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull
    KafkaProducerSettings kafkaProducerSettings,
    @NotNull
    KafkaConsumerSettings kafkaConsumerSettings,
    @NotNull
    KafkaUpdateTopic kafkaUpdateTopic,
    @NotNull
    String micrometer
) {
    public record KafkaProducerSettings(@NotNull String bootstrapServers,
                                        String clientId,
                                        Duration deliveryTimeout,
                                        Integer lingerMs,
                                        Integer batchSize,
                                        Boolean enableIdempotence) {
    }

    public record KafkaConsumerSettings(@NotNull String bootstrapServers,
                                        String groupId,
                                        String autoOffsetReset,
                                        Boolean enableAutoCommit,
                                        Integer concurrency) {
    }

    public record KafkaUpdateTopic(String name,
                                   Integer partitions,
                                   Integer replicas) {
    }
}
