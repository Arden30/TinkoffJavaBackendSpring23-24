package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.dto.request.LinkUpdateRequest;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final TelegramBot telegramBot;
    private final Counter micrometerCounter;

    public void notify(LinkUpdateRequest update) {
        for (var id : update.tgChatIds()) {
            var request = new SendMessage(id, "Link %s:\n%s".formatted(update.url(), update.description()));
            telegramBot.execute(request);
            micrometerCounter.increment();
        }
    }
}
