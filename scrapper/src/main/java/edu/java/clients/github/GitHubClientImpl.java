package edu.java.clients.github;

import edu.java.dto.github.GitHubResponse;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class GitHubClientImpl implements GitHubClient {
    private WebClient webClient;
    @Value("${api.git-hub-url}")
    private String baseUrl;

    private static final String CONTENT = "Content-Type";

    @PostConstruct
    public void init() {
        if (baseUrl.equals("https://api.github.com/")) {
            this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        }
    }

    public GitHubClientImpl(String baseUrl) {
        if (!baseUrl.isEmpty()) {
            this.baseUrl = baseUrl;
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
