package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "starts to track updates on the site by your link";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();

        return new SendMessage(id, "Input your link to track (github/stackoverflow)");
    }
}
