package edu.java.parser;

import edu.java.response.GitHubParsingResponse;
import edu.java.response.ParsingResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubParser extends LinkParser {
    private static final String REGEX = "^https://github.com/([\\w-]+)/([\\w-]+)(/$|$)";

    @Override
    public Optional<ParsingResponse> parse(String url) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(url.trim());
        if (matcher.find()) {
            String name = matcher.group(1);
            String repo = matcher.group(2);

            return Optional.of(new GitHubParsingResponse(name, repo));
        }

        return parseNext(url);
    }
}
