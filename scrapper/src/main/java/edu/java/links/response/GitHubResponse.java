package edu.java.links.response;

public record GitHubResponse(String name, String repo) implements ParsingResponse {
}
