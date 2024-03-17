package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.services.LinkService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCommand implements Command {
    private final LinkService linkService;

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

        List<LinkResponse> linkResponses = linkService.getLinks(id).get().list();

        if (linkResponses.isEmpty()) {
            return new SendMessage(id, "There are no tracked links");
        }

        String links = linkResponses
            .stream()
            .map(LinkResponse::url)
            .map(URI::toString)
            .collect(Collectors.joining("\n"));

        return new SendMessage(id, links);
    }

}
