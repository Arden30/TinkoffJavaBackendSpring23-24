package edu.java.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.commands.Command;
import edu.java.bot.model.State;
import edu.java.bot.services.LinkService;
import edu.java.parser.LinkParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotListener implements UpdatesListener, AutoCloseable {
    private final Map<String, Command> commands;
    private final TelegramBot telegramBot;
    private final LinkService linkService;
    private final Map<Long, State> states = new HashMap<>();
    private final LinkParser linkParser;

    @Autowired
    public BotListener(
        Map<String, Command> commands,
        TelegramBot telegramBot,
        LinkService linkService,
        LinkParser linkParser
    ) {
        this.commands = commands;
        this.telegramBot = telegramBot;
        this.linkService = linkService;
        this.linkParser = linkParser;
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Long id = update.message().chat().id();
            String message = update.message().text();

            State state = states.getOrDefault(id, State.DEFAULT);

            if (!state.equals(State.DEFAULT)) {
                if (linkParser.parse(message).isEmpty()) {
                    telegramBot.execute(new SendMessage(
                        id,
                        "Invalid Link"
                    ));
                    states.put(id, State.DEFAULT);
                    continue;
                }

                telegramBot.execute(handle(update, state));
                continue;
            }

            if (commands.containsKey(message)) {
                State newState = commands.containsKey(message) ? commands.get(message).state() : State.DEFAULT;
                states.put(id, newState);
                telegramBot.execute(commands.get(message).handle(update));
            } else {
                telegramBot.execute(new SendMessage(id, "No such command!"));
            }

        }

        return com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private SendMessage handle(Update update, State state) {
        String message;

        switch (state) {
            case ADD_LINK -> {
                var response =
                    linkService.addLink(update.message().chat().id(), new AddLinkRequest(update.message().text()));
                message = response.isPresent() ? "This link " + update.message().text() + " is being tracked now!"
                    : "Can't track this link";
                states.put(update.message().chat().id(), State.DEFAULT);
            }
            case DELETE_LINK -> {
                var response = linkService.deleteLink(
                    update.message().chat().id(),
                    new RemoveLinkRequest(update.message().text())
                );
                message = response.isPresent() ? "Link " + update.message().text() + " is being untracked now!"
                    : "Can't untrack this link, because it's not being tracked";
                states.put(update.message().chat().id(), State.DEFAULT);
            }
            default -> message = "Something went wrong";
        }

        return new SendMessage(update.message().chat().id(), message);
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
