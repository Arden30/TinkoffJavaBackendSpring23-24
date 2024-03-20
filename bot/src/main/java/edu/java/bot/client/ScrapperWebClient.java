package edu.java.bot.client;

import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.client.dto.response.ListLinksResponse;
import edu.java.bot.configuration.ScrapperConfig;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperWebClient {
    private final WebClient webClient;
    private final static String PATH_TO_CHAT = "tg-chat/{id}";
    private final static String PATH_TO_LINK = "/links";
    private final static String HEADER_NAME = "Tg-Chat-Id";

    public ScrapperWebClient(ScrapperConfig scrapperConfig) {
        this.webClient = WebClient
            .builder()
            .baseUrl(scrapperConfig.baseUrl())
            .build();
    }

    public ScrapperWebClient(String baseUrl) {
        this.webClient = WebClient
            .builder()
            .baseUrl(baseUrl)
            .build();
    }

    public boolean registerChat(long id) {
        return Boolean.TRUE.equals(webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(clientResponse -> Mono.just(!clientResponse.statusCode().isError()))
            .block());
    }

    public boolean deleteChat(Long id) {
        return Boolean.TRUE.equals(webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(PATH_TO_CHAT).build(id))
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(clientResponse -> Mono.just(!clientResponse.statusCode().isError()))
            .block());
    }

    public Optional<ListLinksResponse> getLinks(Long id) {
        return webClient
            .get()
            .uri(PATH_TO_LINK)
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .onErrorResume(exception -> Mono.empty())
            .blockOptional();
    }

    public Optional<LinkResponse> addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(PATH_TO_LINK)
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(exception -> Mono.empty())
            .blockOptional();
    }

    public Optional<LinkResponse> removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(PATH_TO_LINK)
            .accept(MediaType.APPLICATION_JSON)
            .header(HEADER_NAME, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .onErrorResume(exception -> Mono.empty())
            .blockOptional();
    }
}
