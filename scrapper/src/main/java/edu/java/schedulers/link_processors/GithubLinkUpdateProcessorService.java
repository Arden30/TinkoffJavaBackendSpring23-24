package edu.java.schedulers.link_processors;

import edu.java.clients.github.GitHubClient;
import edu.java.model.Link;
import edu.java.repository.GitHubRepository;
import edu.java.repository.LinkRepository;
import edu.java.response.GitHubParsingResponse;
import edu.java.response.ParsingResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final GitHubClient gitHubClient;
    private final GitHubRepository gitHubRepository;

    private final LinkRepository linkRepository;

    @Override
    public Optional<Map.Entry<Link, String>> process(Link link, ParsingResponse resp) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!(resp instanceof GitHubParsingResponse res)) {
            return Optional.empty();
        }

        var response = gitHubClient.fetchUser(res.name(), res.repo());
        if (response.isPresent()) {
            if (link.getUpdatedAt().equals(OffsetDateTime.MIN)) {
                link.setUpdatedAt(response.get().getUpdatedAt());
                linkRepository.saveChanges(link);
            }

            if (!link.getUpdatedAt().withOffsetSameInstant(ZoneOffset.UTC).equals(response.get().getUpdatedAt())) {
                link.setUpdatedAt(response.get().getUpdatedAt());
                stringBuilder.append(response.get().getName()).append(" was updated!").append("\n");
            }

            Long issues = response.get().getIssues();
            Long stars = response.get().getStars();

            var repository = gitHubRepository.findByLinkId(link.getId());
            if (repository.isPresent()) {
                if (!Objects.equals(issues, repository.get().getIssues())) {
                    if (issues > repository.get().getIssues()) {
                        stringBuilder.append("New issue was opened").append("\n");
                    }
                    repository.get().setIssues(issues);
                }

                if (!Objects.equals(stars, repository.get().getStars())) {
                    if (stars > repository.get().getStars()) {
                        stringBuilder.append("New star was added").append("\n");
                    }
                    repository.get().setStars(stars);
                }

                gitHubRepository.saveChanges(repository.get());
            }

            if (!stringBuilder.toString().isEmpty()) {
                return Optional.of(Map.entry(link, stringBuilder.toString()));
            }
        }

        return Optional.empty();
    }
}
