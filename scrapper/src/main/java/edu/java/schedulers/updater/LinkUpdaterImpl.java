package edu.java.schedulers.updater;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.links.parser.LinkParser;
import edu.java.links.response.ParsingResponse;
import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.schedulers.link_processors.LinkUpdateProcessorService;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final BotWebClient botWebClient;
    private final Duration forceCheckDelay;
    private final LinkParser parser;

    private final List<LinkUpdateProcessorService> linkUpdateProcessorService;

    @Override
    @Transactional
    public void update() {
        List<Link> updatedLinks = linkRepository.recentlyUpdated(OffsetDateTime.now().minus(forceCheckDelay));

        List<Map.Entry<Link, String>> linksForNotify = new ArrayList<>();
        for (Link link : updatedLinks) {
            var parserResponse = parseUrl(link.getUrl());

            if (parserResponse.isPresent()) {
                for (LinkUpdateProcessorService service : linkUpdateProcessorService) {
                    Optional<Map.Entry<Link, String>> upd = service.process(link, parserResponse.get());
                    upd.ifPresent(linksForNotify::add);
                }
            }
        }

        linksForNotify.stream().toList()
            .forEach(linkStringEntry -> linkRepository.saveChanges(linkStringEntry.getKey()));
        notifyBot(linksForNotify);
    }

    public Optional<ParsingResponse> parseUrl(String url) {
        return parser.parse(url);
    }

    public void notifyBot(List<Map.Entry<Link, String>> linksWithChanges) {
        for (var link : linksWithChanges) {
            List<Chat> chats = chatRepository.findAllByLink(link.getKey().getId());
            LinkUpdateRequest request = LinkUpdateRequest
                .builder()
                .id(link.getKey().getId())
                .description(link.getValue())
                .tgChatIds(chats.stream().map(Chat::getId).toList())
                .url(URI.create(link.getKey().getUrl()))
                .build();

            botWebClient.sendUpdate(request);
        }
    }
}
