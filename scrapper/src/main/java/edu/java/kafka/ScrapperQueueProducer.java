package edu.java.kafka;

import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void send(LinkUpdateRequest update) {
        try {
            kafkaTemplate.send(applicationConfig.kafkaUpdateTopic().name(), update).whenComplete(
                (sendResult, throwable) -> {
                    if (throwable != null) {
                        log.error("Kafka error: ", throwable);
                    } else {
                        log.info("Successfully sent message to kafka");
                    }
                }
            );
        } catch (Exception ex) {
            log.warn("Cant send message to kafka: ", ex);
        }
    }
}
