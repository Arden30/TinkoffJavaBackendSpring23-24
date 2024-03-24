package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.api.dto.response.ApiErrorResponse;
import edu.java.clients.bot.BotWebClient;
import edu.java.clients.bot.dto.exception.ApiErrorException;
import edu.java.clients.bot.dto.request.LinkUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.net.URI;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@WireMockTest(httpPort = 8080)
public class BotClientTest {
    private BotWebClient botWebClient;
    String baseUrl = "http://localhost:8080/";

    @BeforeEach
    public void setClient() {
        botWebClient = new BotWebClient(baseUrl);
    }

    @Test
    @DisplayName("Sending correct update")
    public void sendUpdate() {
        stubFor(post("/updates")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody("Ok")));

        String actualResponse = botWebClient.sendUpdate(new LinkUpdateRequest(
            1, URI.create("https://my/link"), "description", List.of(1L)
        ));

        assertThat(actualResponse).isEqualTo("Ok");
    }

    @Test
    @DisplayName("Sending update with error code")
    public void invalidBody() {
        String responseBody = """
                {
                    "description":"123",
                    "code":"400",
                    "exceptionName":"123",
                    "exceptionMessage":"123",
                    "stackTrace":[]
                }

            """;

        String expectedDescription = "123";
        String expectedCode = "400";
        String expectedName = "123";
        String expectedMessage = "123";

        stubFor(post("/updates")
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        ApiErrorException thrownException = catchThrowableOfType(
            () -> botWebClient.sendUpdate(new LinkUpdateRequest(1L, null, "1", List.of()
            )),
            ApiErrorException.class
        );
        ApiErrorResponse actualResponse = thrownException.getErrorResponse();

        assertThat(actualResponse.description()).isEqualTo(expectedDescription);
        assertThat(actualResponse.code()).isEqualTo(expectedCode);
        assertThat(actualResponse.exceptionName()).isEqualTo(expectedName);
        assertThat(actualResponse.exceptionMessage()).isEqualTo(expectedMessage);
    }
}
