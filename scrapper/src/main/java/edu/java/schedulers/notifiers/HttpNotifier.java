package edu.java.schedulers.notifiers;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
@RequiredArgsConstructor
public class HttpNotifier implements Notifier {
    private final BotWebClient botClient;

    @Override
    public void notify(LinkUpdateRequest update) {
        log.info("Sending message through HTTP");
        botClient.sendUpdate(update);
    }
}
