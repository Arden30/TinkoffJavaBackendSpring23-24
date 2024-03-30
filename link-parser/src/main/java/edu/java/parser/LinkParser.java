package edu.java.parser;

import edu.java.response.ParsingResponse;
import java.util.Optional;

public abstract class LinkParser {
    protected LinkParser next;

    public static LinkParser createChain(LinkParser first, LinkParser... parsers) {
        LinkParser head = first;

        for (LinkParser next : parsers) {
            head.next = next;
            head = next;
        }

        return first;
    }

    public abstract Optional<ParsingResponse> parse(String url);

    public Optional<ParsingResponse> parseNext(String url) {
        return next == null ? Optional.empty() : next.parse(url);
    }
}
