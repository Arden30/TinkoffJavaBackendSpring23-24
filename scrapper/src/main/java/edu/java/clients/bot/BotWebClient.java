package edu.java.clients.bot;

import edu.java.api.dto.response.ApiErrorResponse;
import edu.java.clients.bot.dto.exception.ApiErrorException;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import edu.java.configuration.BotConfig;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotWebClient {
    private final WebClient webClient;

    public BotWebClient(BotConfig botConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(botConfig.baseUrl())
            .build();
    }

    public BotWebClient(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    public String sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse))))
            .bodyToMono(String.class)
            .block();
    }
}
