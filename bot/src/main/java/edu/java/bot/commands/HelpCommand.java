package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;
import edu.java.bot.services.CommandsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HelpCommand implements Command {
    private final CommandsService commandsService;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "list of available commands for bot";
    }

    @Override
    public State state() {
        return State.DEFAULT;
    }

    @Override
    public SendMessage handle(Update update) {
        StringBuilder response = new StringBuilder();
        response.append("Available commands: \n");
        commandsService.getCommands()
            .forEach((key, value) -> response.append(value.command()).append(": ").append(value.description())
                .append("\n"));
        return new SendMessage(update.message().chat().id(), response.toString());
    }
}
