package edu.java.schedulers.updater;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.links.parser.GitHubParser;
import edu.java.links.parser.LinkParser;
import edu.java.links.parser.StackOverFlowParser;
import edu.java.links.response.GitHubResponse;
import edu.java.links.response.ParsingResponse;
import edu.java.links.response.StackOverFlowResponse;
import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
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
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;
    @Value("${app.scheduler.update}")
    private final Duration update;
    private final LinkParser parser = LinkParser.createChain(new GitHubParser(), new StackOverFlowParser());

    @Override
    @Transactional
    public void update() {
        List<Link> updatedLinks = linkRepository.recentlyUpdated(OffsetDateTime.now().minus(update));
        List<Link> linksForNotify = new ArrayList<>();
        for (Link link : updatedLinks) {
            var parserResponse = parseUrl(link.getUrl());

            if (parserResponse.isPresent()) {
                if (parserResponse.get() instanceof GitHubResponse) {
                    processGitHubLink(link, (GitHubResponse) parserResponse.get()).ifPresent(linksForNotify::add);
                } else {
                    processStackOverflowLink(link, (StackOverFlowResponse) parserResponse.get()).ifPresent(
                        linksForNotify::add);
                }
            }
        }

        linksForNotify.forEach(linkRepository::saveChanges);
        notifyBot(linksForNotify);
    }

    public Optional<Link> processGitHubLink(Link link, GitHubResponse resp) {
        var repository = gitHubClient.fetchUser(resp.name(), resp.repo());
        if (repository.isPresent() && !link.getUpdatedAt().equals(repository.get().getUpdatedAt())) {
            link.setUpdatedAt(repository.get().getUpdatedAt());

            return Optional.of(link);
        }

        return Optional.empty();
    }

    public Optional<Link> processStackOverflowLink(Link link, StackOverFlowResponse resp) {
        var question = stackOverFlowClient.fetchUser(Long.parseLong(resp.questionId()));
        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().getUpdatedAt())) {
            link.setUpdatedAt(question.get().getUpdatedAt());

            return Optional.of(link);
        }

        return Optional.empty();
    }

    public Optional<ParsingResponse> parseUrl(String url) {
        return parser.parse(url);
    }

    public void notifyBot(List<Link> links) {
        for (Link link : links) {
            List<Chat> chats = chatRepository.findAllByLink(link.getId());
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
