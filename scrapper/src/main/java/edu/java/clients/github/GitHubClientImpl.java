package edu.java.clients.github;

import edu.java.clients.github.dto.GitHubResponse;
import edu.java.configuration.GitHubConfig;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class GitHubClientImpl implements GitHubClient {
    private WebClient webClient;
    private static final String CONTENT = "Content-Type";

    public GitHubClientImpl(GitHubConfig gitHubConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(gitHubConfig.baseUrl())
            .defaultHeader(CONTENT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public GitHubClientImpl(String baseUrl) {
        if (!baseUrl.isEmpty()) {
            this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        }
    }

    @Override
    public GitHubResponse fetchUser(String user, String repo) {
        return webClient.get()
            .uri("/repos/{user}/{repo}", user, repo)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
    }
}
