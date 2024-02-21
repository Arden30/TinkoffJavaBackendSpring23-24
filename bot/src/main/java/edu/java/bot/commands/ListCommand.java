package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.User;
import edu.java.bot.services.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCommand implements Command {
    private final UserService userService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "shows links which are tracked";
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            return new SendMessage(id, "Please, register (input command /start)");
        }

        return new SendMessage(id, createResponse(userOptional.get().getLinks()));
    }

    private String createResponse(List<URI> uriList) {
        StringBuilder response = new StringBuilder();

        if (uriList.isEmpty()) {
            return "List is empty";
        }

        response.append("Your links: \n");
        uriList.forEach(uri -> response.append(uri.toString()).append("\n"));

        return response.toString();
    }
}
