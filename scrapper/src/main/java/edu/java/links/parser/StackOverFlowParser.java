package edu.java.links.parser;

import edu.java.links.response.ParsingResponse;
import edu.java.links.response.StackOverFlowParsingResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverFlowParser extends LinkParser {
    private static final String REGEX = "^https://stackoverflow\\.com/questions/(\\d+)(/|$)";

    @Override
    public Optional<ParsingResponse> parse(String url) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(url.trim());
        if (matcher.find()) {
            String questionId = matcher.group(1);
            return Optional.of(new StackOverFlowParsingResponse(questionId));
        }
        return parseNext(url);
    }
}
