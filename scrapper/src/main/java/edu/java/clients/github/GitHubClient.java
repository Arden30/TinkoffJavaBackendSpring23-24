package edu.java.clients.github;

import edu.java.dto.github.GitHubResponse;

public interface GitHubClient {
    GitHubResponse fetchUser(String user, String repo);
}
