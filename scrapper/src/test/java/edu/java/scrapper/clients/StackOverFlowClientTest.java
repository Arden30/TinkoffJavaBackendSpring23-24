package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.stackoverflow.StackOverFlowClient;
import edu.java.clients.stackoverflow.StackOverFlowClientImpl;
import edu.java.clients.stackoverflow.dto.StackOverFlowQuestion;
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
        stubFor(get("/questions/" + id + "?site=stackoverflow")
            .willReturn(okJson(
                getResponseBody(id, name)
            )));

        StackOverFlowClient stackOverFlowClient = new StackOverFlowClientImpl(baseUrl);
        StackOverFlowQuestion stackOverFlowResponse = stackOverFlowClient.fetchUser(id).get();

        assertThat(stackOverFlowResponse.getStackOverFlowOwner().getDisplayName()).isEqualTo(name);
        assertThat(stackOverFlowResponse.getId()).isEqualTo(id);
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
                               "last_activity_date": "2023-09-13T21:17:36Z"
                           }
                   ],
               "has_more": false,
               "quota_max": 300,
               "quota_remaining": 299
            }""", id, name);
    }
}
