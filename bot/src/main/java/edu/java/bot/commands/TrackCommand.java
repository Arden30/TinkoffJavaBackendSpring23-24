package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    @Override
    public String command() {
        return "/track";
    }

    @Override
    public State state() {
        return State.ADD_LINK;
    }

    @Override
    public String description() {
        return "starts to track updates on the site by your link";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();

        return new SendMessage(id, "Input your link to track");
    }
}
