package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.dto.github.GitHubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {
    String baseUrl = "http://localhost:8080/";
    String name = "user";
    String repository = "repo";

    @Test
    @DisplayName("GitHub client test")
    void test() {
        stubFor(get("/repos/user/repo")
            .willReturn(okJson(
                getResponseBody(name, repository)
            )));

        GitHubClient gitHubClient = new GitHubClientImpl(baseUrl);
        GitHubResponse gitHubResponse = gitHubClient.fetchUser(name, repository);

        assertThat(gitHubResponse.getGitHubOwner().getLogin()).isEqualTo(name);
        assertThat(gitHubResponse.getName()).isEqualTo(repository);
    }

    String getResponseBody(String name, String repo) {
        return String.format("""
            {
            "id":1,
            "name":"%s",
            "description": "test description",
            "owner": {
                "id":2,
                "login":"%s"
            },
            "url":"https://github.com/user/repo",
            "created_at":"2023-09-13T21:17:36Z"
            }""", repo, name);
    }
}
