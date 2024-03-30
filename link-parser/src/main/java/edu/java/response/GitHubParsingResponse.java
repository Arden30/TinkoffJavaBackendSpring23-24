package edu.java.response;

public record GitHubParsingResponse(String name, String repo) implements ParsingResponse {
}
