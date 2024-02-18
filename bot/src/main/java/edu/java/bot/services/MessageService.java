package edu.java.bot.services;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.links.LinkValidator;
import edu.java.bot.model.State;
import edu.java.bot.model.User;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final UserService userService;

    private final LinkValidator linkValidator;

    private final static String INVALID = "Invalid link";

    public SendMessage addLink(long id, String link) {
        URI uri = URI.create(link);
        User user = userService.findById(id).get();
        if (linkValidator.isValid(uri)) {
            List<URI> uriList = user.getLinks();
            if (uriList.contains(uri)) {
                user.setState(State.DEFAULT);

                return new SendMessage(id, "This link is already being tracked!");
            } else {
                uriList.add(uri);
                user.setState(State.DEFAULT);

                return new SendMessage(id, "Now your link is being tracked:\n" + link);
            }
        }

        user.setState(State.DEFAULT);
        return new SendMessage(id, INVALID);
    }

    public SendMessage deleteLink(long id, String link) {
        URI uri = URI.create(link);
        User user = userService.findById(id).get();
        if (linkValidator.isValid(uri)) {
            if (!user.getLinks().remove(uri)) {
                user.setState(State.DEFAULT);
                return new SendMessage(id, "You don't have this link in your tracked links");
            }
            user.setState(State.DEFAULT);

            return new SendMessage(id, "Link was deleted:\n" + link);
        }

        user.setState(State.DEFAULT);
        return new SendMessage(id, INVALID);
    }
}
