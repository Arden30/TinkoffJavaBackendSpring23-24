package edu.java.clients.github;

import edu.java.clients.github.dto.GitHubResponse;
import java.util.Optional;

public interface GitHubClient {
    Optional<GitHubResponse> fetchUser(String user, String repo);
}
