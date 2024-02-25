package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class GitHubResponse {
    String name;
    @JsonProperty("owner")
    GitHubOwner gitHubOwner;
    @JsonProperty("created_at")
    OffsetDateTime createdAt;
}
