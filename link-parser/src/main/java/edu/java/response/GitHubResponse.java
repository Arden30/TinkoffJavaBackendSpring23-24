package edu.java.response;

public record GitHubResponse(String name, String repo) implements ParsingResponse {
}
