package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.clients.stackoverflow.StackOverFlowClientImpl;
import edu.java.dto.stackoverflow.StackOverFlowResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest(httpPort = 8080)
public class StackOverFlowClientTest {
    String baseUrl = "http://localhost:8080/";
    String name = "name";
    long id = 1;
    @Test
    @DisplayName("StackOverFlow client test")
    void test() {
        stubFor(get("/questions/" + id)
            .willReturn(okJson(
                getResponseBody(id, name)
            )));

        StackOverFlowClient stackOverFlowClient = new StackOverFlowClientImpl(baseUrl);
        StackOverFlowResponse stackOverFlowResponse = stackOverFlowClient.fetchUser(id);

        assertThat(stackOverFlowResponse.getStackOverFlowQuestions().get(0).getStackOverFlowOwner().getDisplayName()).isEqualTo(name);
        assertThat(stackOverFlowResponse.getStackOverFlowQuestions().get(0).getId()).isEqualTo(id);
    }

    String getResponseBody(long id, String name) {
        return String.format("""
            {
               "items":[
                           {
                               "id": %d,
                               "owner": {
                                   "display_name":"%s"
                               },
                               "title": "question",
                               "is_answered": true,
                               "answer_count": 3,
                               "body": "test question body",
                               "creation_date": "2023-09-13T21:17:36Z"
                           }
                   ],
               "has_more": false,
               "quota_max": 300,
               "quota_remaining": 299
            }""", id, name);
    }
}
