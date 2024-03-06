package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.client.dto.exceptions.ApiErrorException;
import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.client.dto.response.ListLinksResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@WireMockTest(httpPort = 8090)
public class ScrapperClientTest {
    private ScrapperWebClient scrapperWebClient;
    String baseUrl = "http://localhost:8090/";
    private final static String INVALID_BODY = """
                {
                    "description":"123",
                    "code":"400",
                    "exceptionName":"123",
                    "exceptionMessage":"123",
                    "stackTrace":[]
                }
            """;

    private final static String NOT_FOUND_BODY = """
                {
                    "description":"123",
                    "code":"404",
                    "exceptionName":"123",
                    "exceptionMessage":"123",
                    "stackTrace":[
                        "1",
                        "2",
                        "3"
                    ]
                }
            """;

    private final static String LIST_OF_LINKS = """
                {
                    "list":[
                        {
                            "id":1,
                            "url":"link"
                        }
                    ]
                }
        """;

    private final static String LINK = """
            {
                "id":1,
                "url":"link"
            }
            """;

    @BeforeEach
    public void setClient() {
        scrapperWebClient = new ScrapperWebClient(baseUrl);
    }

    @Test
    @DisplayName("Successful registration")
    public void registerChat(){
        String responseBody = "Chat with id 1 was successfully registered!";
        stubFor(post("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        String actualResponse = scrapperWebClient.registerChat(1L);

        assertThat(actualResponse).isEqualTo(responseBody);
    }

    @Test
    @DisplayName("Twice registration (error)")
    public void registerChatTwice(){
        String responseBody = "Chat with id 1 was successfully registered!";
        stubFor(post("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        scrapperWebClient.registerChat(1L);

        stubFor(post("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(INVALID_BODY)));

        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.registerChat(1L),
            ApiErrorException.class
        );

        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    @DisplayName("Successful deleting of chat")
    public void deleteChat() {
        String responseBody = "Chat with id 1 was successfully deleted!";
        stubFor(delete("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        String actualResponse = scrapperWebClient.deleteChat(1L);

        assertThat(actualResponse).isEqualTo(responseBody);
    }

    @Test
    @DisplayName("Chat to delete not found")
    public void deleteChatNotFound() {
        stubFor(delete("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.deleteChat(1L),
            ApiErrorException.class
        );

        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    @DisplayName("Get links test")
    public void getLinks() {
        stubFor(get("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(LIST_OF_LINKS)));

        ListLinksResponse actualResponse = scrapperWebClient.getLinks(1L);

        assertThat(actualResponse.list().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Links were not found")
    public void getLinksNotFound(){
        stubFor(get("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.getLinks(1L),
            ApiErrorException.class
        );

        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    @DisplayName("Successful adding of link")
    public void addLink() throws URISyntaxException {
        stubFor(post("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(LINK)));

        LinkResponse actualResponse = scrapperWebClient.addLink(
            1L, new AddLinkRequest(new URI("link"))
        );

        assertThat(actualResponse.url().getPath()).isEqualTo("link");
    }

    @Test
    @DisplayName("Link not found while adding")
    public void addLinkNotFound() {
        stubFor(post("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.addLink(1L, new AddLinkRequest(new URI("123"))),
            ApiErrorException.class
        );

        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }

    @Test
    @DisplayName("Successful remove of link")
    public void removeLink() throws URISyntaxException {
        stubFor(delete("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(LINK)));

        LinkResponse actualResponse = scrapperWebClient.removeLink(
            1L, new RemoveLinkRequest(new URI("link"))
        );

        assertThat(actualResponse.url().getPath()).isEqualTo("link");
    }

    @Test
    @DisplayName("Link to remove not found")
    public void removeLinkNotFound() {
        stubFor(delete("/links")
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(NOT_FOUND_BODY)));

        Throwable actualException = catchThrowableOfType(
            () -> scrapperWebClient.removeLink(1L, new RemoveLinkRequest(new URI("link"))),
            ApiErrorException.class
        );

        assertThat(actualException)
            .isInstanceOf(ApiErrorException.class);
    }
}
