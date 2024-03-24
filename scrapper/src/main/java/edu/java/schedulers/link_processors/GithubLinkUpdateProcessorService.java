package edu.java.schedulers.link_processors;

import edu.java.clients.github.GitHubClient;
import edu.java.links.response.GitHubResponse;
import edu.java.links.response.ParsingResponse;
import edu.java.model.Link;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubLinkUpdateProcessorService implements LinkUpdateProcessorService {
    private final GitHubClient gitHubClient;

    @Override
    public Optional<Link> process(Link link, ParsingResponse resp) {
        if (!(resp instanceof GitHubResponse response)) {
            return Optional.empty();
        }

        var repository = gitHubClient.fetchUser(response.name(), response.repo());
        if (repository.isPresent() && !link.getUpdatedAt().equals(repository.get().getUpdatedAt())) {
            link.setUpdatedAt(repository.get().getUpdatedAt());

            return Optional.of(link);
        }

        return Optional.empty();
    }
}
