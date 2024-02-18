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

        if (userOptional.isPresent()) {
            StringBuilder response = new StringBuilder();
            List<URI> uriList = userOptional.get().getLinks();
            if (uriList.isEmpty()) {
                return new SendMessage(id, "List is empty");
            }

            response.append("Your links: \n");
            uriList.forEach(uri -> response.append(uri.toString()).append("\n"));

            return new SendMessage(id, response.toString());
        }

        return new SendMessage(id, "Please, register (input command /start)");
    }
}
