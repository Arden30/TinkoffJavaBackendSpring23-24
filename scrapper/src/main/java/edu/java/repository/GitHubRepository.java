package edu.java.repository;

import edu.java.model.GitHubRepo;
import java.util.Optional;

public interface GitHubRepository {
    Optional<GitHubRepo> findByLinkId(long linkId);

    void addRepo(GitHubRepo gitHubRepo);

    void saveChanges(GitHubRepo gitHubRepo);

    boolean delete(long linkId);
}
