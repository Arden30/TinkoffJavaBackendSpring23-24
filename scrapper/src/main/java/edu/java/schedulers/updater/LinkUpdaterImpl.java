package edu.java.schedulers.updater;

import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.links.parser.LinkParser;
import edu.java.links.response.GitHubParsingResponse;
import edu.java.links.response.ParsingResponse;
import edu.java.links.response.StackOverFlowParsingResponse;
import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.repository.ChatRepository;
import edu.java.repository.GitHubRepository;
import edu.java.repository.LinkRepository;
import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
    private final GitHubRepository gitHubRepository;
    private final BotWebClient botWebClient;
    private final GitHubClient gitHubClient;
    private final StackOverFlowClient stackOverFlowClient;
    private final Duration forceCheckDelay;
    private final LinkParser parser;

    @Override
    @Transactional
    public void update() {
        List<Link> updatedLinks = linkRepository.recentlyUpdated(OffsetDateTime.now().minus(forceCheckDelay));

        List<Map.Entry<Link, String>> linksForNotify = new ArrayList<>();
        for (Link link : updatedLinks) {
            var parserResponse = parseUrl(link.getUrl());

            if (parserResponse.isPresent()) {
                if (parserResponse.get() instanceof GitHubParsingResponse) {
                    processGitHubLink(
                        link,
                        (GitHubParsingResponse) parserResponse.get()
                    ).ifPresent(linksForNotify::add);
                } else {
                    processStackOverflowLink(link, (StackOverFlowParsingResponse) parserResponse.get()).ifPresent(
                        linksForNotify::add);
                }
            }
        }

        linksForNotify.stream().toList()
            .forEach(linkStringEntry -> linkRepository.saveChanges(linkStringEntry.getKey()));
        notifyBot(linksForNotify);
    }

    public Optional<Map.Entry<Link, String>> processGitHubLink(Link link, GitHubParsingResponse resp) {
        StringBuilder stringBuilder = new StringBuilder();
        var response = gitHubClient.fetchUser(resp.name(), resp.repo());
        if (response.isPresent()) {
            if (!link.getUpdatedAt().withOffsetSameInstant(ZoneOffset.UTC).equals(response.get().getUpdatedAt())) {
                link.setUpdatedAt(response.get().getUpdatedAt());
                stringBuilder.append(response.get().getName()).append(" was updated!").append("\n");
            }

            Long issues = response.get().getIssues();
            Long stars = response.get().getStars();

            var repository = gitHubRepository.findByLinkId(link.getId());
            if (repository.isPresent()) {
                if (issues > repository.get().getIssues()) {
                    repository.get().setIssues(issues);
                    stringBuilder.append("New issue was opened").append("\n");
                }

                if (stars > repository.get().getStars()) {
                    repository.get().setStars(stars);
                    stringBuilder.append("New star was added").append("\n");
                }

                gitHubRepository.saveChanges(repository.get());
            }

            if (!stringBuilder.toString().isEmpty()) {
                return Optional.of(Map.entry(link, stringBuilder.toString()));
            }
        }

        return Optional.empty();
    }

    public Optional<Map.Entry<Link, String>> processStackOverflowLink(Link link, StackOverFlowParsingResponse resp) {
        var question = stackOverFlowClient.fetchUser(Long.parseLong(resp.questionId()));
        if (question.isPresent() && !link.getUpdatedAt().equals(question.get().getUpdatedAt())) {
            link.setUpdatedAt(question.get().getUpdatedAt());

            return Optional.of(Map.entry(link, "Question " + question.get().getId() + " was updated"));
        }

        return Optional.empty();
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
