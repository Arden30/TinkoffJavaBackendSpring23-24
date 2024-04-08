package edu.java.bot.listener;

import edu.java.bot.api.dto.request.LinkUpdateRequest;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUpdatesListener {
    private final NotificationService notificationService;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    private final ApplicationConfig applicationConfig;

    @KafkaListener(topics = "${app.kafka-update-topic.name}",
                   groupId = "mainConsumerGroup",
                   containerFactory = "containerFactory")
    public void listen(@Payload LinkUpdateRequest update) {
        try {
            notificationService.notify(update);
        } catch (Exception e) {
            kafkaTemplate.send(applicationConfig.kafkaUpdateTopic().name() + "_dlq", update);
        }
    }
}
