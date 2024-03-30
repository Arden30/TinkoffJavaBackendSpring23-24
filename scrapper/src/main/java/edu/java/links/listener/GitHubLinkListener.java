package edu.java.links.listener;

import edu.java.clients.github.GitHubClient;
import edu.java.model.GitHubRepo;
import edu.java.model.Link;
import edu.java.parser.LinkParser;
import edu.java.repository.GitHubRepository;
import edu.java.response.GitHubParsingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GitHubLinkListener implements LinkListener {
    private final LinkParser linkParser;
    private final GitHubRepository gitHubRepository;
    private final GitHubClient gitHubClient;

    @Override
    public void onLinkAdd(Link link) {
        var parserResponse = linkParser.parse(link.getUrl());

        if (parserResponse.isPresent()) {
            var parseResp = parserResponse.get();

            if (parseResp instanceof GitHubParsingResponse) {
                var gitHubResponse = gitHubClient.fetchUser(
                    ((GitHubParsingResponse) parseResp).name(),
                    ((GitHubParsingResponse) parseResp).repo()
                );

                GitHubRepo gitHubRepo = new GitHubRepo();
                if (gitHubResponse.isPresent()) {
                    gitHubRepo.setLinkId(link.getId());
                    gitHubRepo.setStars(gitHubResponse.get().getStars());
                    gitHubRepo.setIssues(gitHubResponse.get().getIssues());
                }

                gitHubRepository.addRepo(gitHubRepo);
            }
        }
    }

    @Override
    public void onLinkRemove(Link link) {
        var parserResponse = linkParser.parse(link.getUrl());

        if (parserResponse.isPresent()) {
            var parseResp = parserResponse.get();

            if (parseResp instanceof GitHubParsingResponse) {
                gitHubRepository.delete(link.getId());
            }
        }
    }
}
