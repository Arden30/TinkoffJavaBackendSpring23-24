package edu.java.services.jooq;

import edu.java.clients.github.GitHubClient;
import edu.java.links.parser.LinkParser;
import edu.java.links.response.GitHubParsingResponse;
import edu.java.model.GitHubRepo;
import edu.java.model.Link;
import edu.java.repository.jooq.JooqGitHubRepository;
import edu.java.services.LinkTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class JooqLinkTypeService implements LinkTypeService {
    private final LinkParser linkParser;
    private final GitHubClient gitHubClient;
    private final JooqGitHubRepository jooqGitHubRepository;

    @Override
    @Transactional
    public void add(Link link) {
        var parserResponse = linkParser.parse(link.getUrl());

        if (parserResponse.isPresent()) {
            var parseResp = parserResponse.get();

            if (parseResp instanceof GitHubParsingResponse) {
                var gitHubResponse = gitHubClient.fetchUser(
                    ((GitHubParsingResponse) parseResp).name(),
                    ((GitHubParsingResponse) parseResp).repo()
                );

                if (gitHubResponse.isPresent()) {
                    GitHubRepo gitHubRepo = new GitHubRepo();
                    gitHubRepo.setLinkId(link.getId());
                    gitHubRepo.setStars(gitHubResponse.get().getStars());
                    gitHubRepo.setIssues(gitHubResponse.get().getIssues());

                    jooqGitHubRepository.addRepo(gitHubRepo);
                }
            }
        }
    }

    @Override
    @Transactional
    public void remove(Link link) {
        var parserResponse = linkParser.parse(link.getUrl());

        if (parserResponse.isPresent()) {
            var parseResp = parserResponse.get();

            if (parseResp instanceof GitHubParsingResponse) {
                jooqGitHubRepository.delete(link.getId());
            }
        }
    }
}
