package edu.java.clients.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class GitHubResponse {
    @JsonProperty("full_name")
    String name;
    @JsonProperty("owner")
    GitHubOwner gitHubOwner;
    @JsonProperty("pushed_at")
    OffsetDateTime updatedAt;
    @JsonProperty("stargazers_count")
    Long stars;
    @JsonProperty("open_issues_count")
    Long issues;
}
