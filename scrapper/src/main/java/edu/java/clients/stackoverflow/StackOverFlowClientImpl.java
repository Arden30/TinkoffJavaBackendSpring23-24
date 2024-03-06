package edu.java.clients.stackoverflow;

import edu.java.clients.stackoverflow.dto.StackOverFlowResponse;
import edu.java.configuration.StackOverFlowConfig;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@NoArgsConstructor
public class StackOverFlowClientImpl implements StackOverFlowClient {
    private WebClient webClient;
    private static final String CONTENT = "Content-Type";

    public StackOverFlowClientImpl(StackOverFlowConfig stackOverFlowConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(stackOverFlowConfig.baseUrl())
            .defaultHeader(CONTENT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    public StackOverFlowClientImpl(String baseUrl) {
        if (!baseUrl.isEmpty()) {
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
