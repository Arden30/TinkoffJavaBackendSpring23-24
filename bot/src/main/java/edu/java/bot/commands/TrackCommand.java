package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.State;
import edu.java.bot.services.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final UserService userService;

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
        if (userService.findById(id).isEmpty()) {
            return new SendMessage(id, "Please, register (input command /start)");
        }
        userService.changeState(id, State.ADD_LINK);

        return new SendMessage(id, "Input your link to track (github/stackoverflow)");
    }
}
