package edu.java.configuration;

import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final ApplicationConfig applicationConfig;

    @Bean
    public ProducerFactory<String, LinkUpdateRequest> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        ApplicationConfig.KafkaProducerSettings kafkaSettings = applicationConfig.kafkaProducerSettings();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaSettings.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaSettings.clientId());
        props.put(
            ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            (int) kafkaSettings.deliveryTimeout().toMillis()
        );
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaSettings.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaSettings.batchSize());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaSettings.enableIdempotence());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateRequest>
    kafkaTemplate(ProducerFactory<String, LinkUpdateRequest> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic linkUpdate() {
        return TopicBuilder
            .name(applicationConfig.kafkaUpdateTopic().name())
            .partitions(applicationConfig.kafkaUpdateTopic().partitions())
            .replicas(applicationConfig.kafkaUpdateTopic().replicas())
            .build();
    }
}
