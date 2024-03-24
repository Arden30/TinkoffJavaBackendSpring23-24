package edu.java.schedulers.updater;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.links.parser.GitHubParser;
import edu.java.links.parser.LinkParser;
import edu.java.links.parser.StackOverFlowParser;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUpdaterImpl implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final ChatRepository chatRepository;
    private final BotWebClient botWebClient;
    @Value("${app.scheduler.force-check-delay}")
    private final Duration update;
    private final LinkParser parser = LinkParser.createChain(new GitHubParser(), new StackOverFlowParser());
    private final List<LinkUpdateProcessorService> linkUpdateProcessorService;

    @Override
    @Transactional
    public void update() {
        List<Link> updatedLinks = linkRepository.recentlyUpdated(OffsetDateTime.now().minus(update));
        List<Link> linksForNotify = new ArrayList<>();
        for (Link link : updatedLinks) {
            var parserResponse = parseUrl(link.getUrl());

            if (parserResponse.isPresent()) {
                for (LinkUpdateProcessorService service : linkUpdateProcessorService) {
                    Optional<Link> upd = service.process(link, parserResponse.get());
                    upd.ifPresent(linksForNotify::add);
                }
            }
        }

        linksForNotify.forEach(linkRepository::save);
        notifyBot(linksForNotify);
    }

    public Optional<ParsingResponse> parseUrl(String url) {
        return parser.parse(url);
    }

    public void notifyBot(List<Link> links) {
        for (Link link : links) {
            List<Chat> chats = chatRepository.findChatsByLinksId(link.getId());
            LinkUpdateRequest request = LinkUpdateRequest
                .builder()
                .id(link.getId())
                .description("Link was updated!")
                .tgChatIds(chats.stream().map(Chat::getId).toList())
                .url(URI.create(link.getUrl()))
                .build();

            botWebClient.sendUpdate(request);
        }
    }
}
