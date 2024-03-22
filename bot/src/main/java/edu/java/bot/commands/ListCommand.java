package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.client.dto.response.ListLinksResponse;
import edu.java.bot.model.State;
import edu.java.bot.services.LinkService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
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
    public State state() {
        return State.DEFAULT;
    }

    @Override
    public SendMessage handle(Update update) {
        Long id = update.message().chat().id();
        Optional<ListLinksResponse> linkResponses = linkService.getLinks(id);

        if (linkResponses.isEmpty()) {
            return new SendMessage(id, "There are no tracked links");
        }

        List<LinkResponse> list = linkResponses.get().list();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Your links: \n");

        String links = list
            .stream()
            .map(LinkResponse::url)
            .map(URI::toString)
            .collect(Collectors.joining("\n"));

        stringBuilder.append(links);

        return new SendMessage(id, stringBuilder.toString());
    }

}
