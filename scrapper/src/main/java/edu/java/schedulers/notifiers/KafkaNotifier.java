package edu.java.schedulers.notifiers;

import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.kafka.ScrapperQueueProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
@RequiredArgsConstructor
public class KafkaNotifier implements Notifier {
    private final ScrapperQueueProducer scrapperQueueProducer;

    @Override
    public void notify(LinkUpdateRequest update) {
        log.info("Sending message through Kafka");
        scrapperQueueProducer.send(update);
    }
}
