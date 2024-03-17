package edu.java.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.commands.Command;
import edu.java.bot.model.State;
import edu.java.bot.model.UserRequest;
import edu.java.bot.services.LinkService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotListener implements UpdatesListener, AutoCloseable {
    private final Map<String, Command> commands;
    private final TelegramBot telegramBot;
    private final LinkService linkService;

    @Autowired
    public BotListener(
        Map<String, Command> commands,
        TelegramBot telegramBot,
        LinkService linkService
    ) {
        this.commands = commands;
        this.telegramBot = telegramBot;
        this.linkService = linkService;
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            State state;
            if (update.message().text().startsWith("/track")) {
                state = State.ADD_LINK;
                telegramBot.execute(handle(update, state).request());
            } else if (update.message().text().startsWith("/untrack")) {
                state = State.DELETE_LINK;
                telegramBot.execute(handle(update, state).request());
            } else {
                if (commands.containsKey(update.message().text())) {
                    telegramBot.execute(commands.get(update.message().text()).handle(update));
                } else {
                    telegramBot.execute(new SendMessage(update.message().chat().id(), "No such command"));
                }
            }
        }

        return com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private UserRequest handle(Update update, State state) {
        String message;

        switch (state) {
            case ADD_LINK -> {
                var response =
                    linkService.addLink(update.message().chat().id(), new AddLinkRequest(update.message().text()));
                message = response.isPresent() ? "This link " + update.message().text() + " is being tracked now!"
                    : "Can't track this link";
            }
            case DELETE_LINK -> {
                var response = linkService.deleteLink(
                    update.message().chat().id(),
                    new RemoveLinkRequest(update.message().text())
                );
                message = response.isPresent() ? "Link " + update.message().text() + " is being untracked now!"
                    : "Can't untrack this link";
            }
            default -> message = "No command";
        }

        return UserRequest.builder()
            .request(new SendMessage(update, message))
            .build();
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
