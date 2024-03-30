package edu.java.repository.jooq;

import edu.java.domain.jooq.tables.records.GithubReposRecord;
import edu.java.model.GitHubRepo;
import edu.java.repository.GitHubRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.GithubRepos.GITHUB_REPOS;

@RequiredArgsConstructor
public class JooqGitHubRepository implements GitHubRepository {
    private final DSLContext dsl;

    @Override
    public Optional<GitHubRepo> findByLinkId(long linkId) {
        return dsl.selectFrom(GITHUB_REPOS)
            .where(GITHUB_REPOS.LINK_ID.eq(linkId))
            .fetchOptional(this::mapToRepo);
    }

    @Override
    public void addRepo(GitHubRepo gitHubRepo) {
        dsl.insertInto(GITHUB_REPOS, GITHUB_REPOS.LINK_ID, GITHUB_REPOS.STARS, GITHUB_REPOS.ISSUES)
            .values(gitHubRepo.getLinkId(), gitHubRepo.getStars(), gitHubRepo.getIssues())
            .execute();
    }

    @Override
    public void saveChanges(GitHubRepo gitHubRepo) {
        dsl.update(GITHUB_REPOS)
            .set(GITHUB_REPOS.LINK_ID, gitHubRepo.getLinkId())
            .set(GITHUB_REPOS.STARS, gitHubRepo.getStars())
            .set(GITHUB_REPOS.ISSUES, gitHubRepo.getIssues())
            .where(GITHUB_REPOS.LINK_ID.eq(gitHubRepo.getLinkId()))
            .execute();
    }

    @Override
    public boolean delete(long linkId) {
        return dsl.deleteFrom(GITHUB_REPOS)
            .where(GITHUB_REPOS.LINK_ID.eq(linkId))
            .execute() == 1;
    }

    public GitHubRepo mapToRepo(GithubReposRecord githubReposRecord) {
        GitHubRepo gitHubRepo = new GitHubRepo();

        gitHubRepo.setLinkId(githubReposRecord.getLinkId());
        gitHubRepo.setStars(githubReposRecord.getStars());
        gitHubRepo.setIssues(githubReposRecord.getIssues());

        return gitHubRepo;
    }
}
