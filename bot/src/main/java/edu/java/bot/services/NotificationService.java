package edu.java.bot.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.api.dto.request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final TelegramBot telegramBot;

    public void notify(LinkUpdateRequest update) {
        for (var id : update.tgChatIds()) {
            var request = new SendMessage(id, "Link %s:\n%s".formatted(update.url(), update.description()));
            telegramBot.execute(request);
        }
    }
}
