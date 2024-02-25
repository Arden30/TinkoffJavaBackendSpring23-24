package edu.java.clients.stackoverflow;

import edu.java.dto.stackoverflow.StackOverFlowResponse;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class StackOverFlowClientImpl implements StackOverFlowClient {
    private WebClient webClient;
    @Value("${api.stack-overflow-url}")
    private String baseUrl;
    private static final String CONTENT = "Content-Type";

    @PostConstruct
    public void init() {
        if (baseUrl.equals("https://api.stackexchange.com/2.3/")) {
            this.webClient = WebClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader(CONTENT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        }
    }

    public StackOverFlowClientImpl(String baseUrl) {
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
    public StackOverFlowResponse fetchUser(long id) {
        return webClient.get()
            .uri("/questions/{id}", id)
            .retrieve()
            .bodyToMono(StackOverFlowResponse.class)
            .block();
    }
}
