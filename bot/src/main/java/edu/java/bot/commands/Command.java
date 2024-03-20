package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;

public interface Command {
    String command();

    String description();

    State state();

    SendMessage handle(Update update);
}
