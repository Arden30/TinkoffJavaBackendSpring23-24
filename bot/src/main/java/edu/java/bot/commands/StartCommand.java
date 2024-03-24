package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;
import edu.java.bot.services.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartCommand implements Command {

    private final UserService userService;

    @Override
    public State state() {
        return State.DEFAULT;
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "register in WebSitesTracker";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        if (userService.register(id)) {
            return new SendMessage(id, "You were successfully registered!");
        }

        return new SendMessage(id, "You are already registered");
    }
}
