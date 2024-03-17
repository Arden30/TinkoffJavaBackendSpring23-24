package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "deletes link from list of tracked";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();

        return new SendMessage(id, "Input your link to untrack");
    }
}
