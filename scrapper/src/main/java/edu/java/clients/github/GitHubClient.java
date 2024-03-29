package edu.java.clients.github;

import edu.java.clients.github.dto.GitHubResponse;

public interface GitHubClient {
    GitHubResponse fetchUser(String user, String repo);
}
