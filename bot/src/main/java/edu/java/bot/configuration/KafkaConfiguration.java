package edu.java.bot.configuration;

import edu.java.bot.api.dto.request.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
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
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        ApplicationConfig.KafkaConsumerSettings kafkaConsumerSettings = applicationConfig.kafkaConsumerSettings();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerSettings.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerSettings.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerSettings.autoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerSettings.enableAutoCommit());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> containerFactory(
        ConsumerFactory<String, LinkUpdateRequest> consumerFactory
    ) {
        ApplicationConfig.KafkaConsumerSettings kafkaConsumerSettings = applicationConfig.kafkaConsumerSettings();
        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(kafkaConsumerSettings.concurrency());

        return factory;
    }

    @Bean
    public NewTopic linkUpdate() {
        return TopicBuilder
            .name(applicationConfig.kafkaUpdateTopic().name())
            .partitions(applicationConfig.kafkaUpdateTopic().partitions())
            .replicas(applicationConfig.kafkaUpdateTopic().replicas())
            .build();
    }

    @Bean
    public NewTopic linkUpdateDlq() {
        return TopicBuilder
            .name(applicationConfig.kafkaUpdateTopic().name() + "_dlq")
            .partitions(applicationConfig.kafkaUpdateTopic().partitions())
            .replicas(applicationConfig.kafkaUpdateTopic().replicas())
            .build();
    }
}
