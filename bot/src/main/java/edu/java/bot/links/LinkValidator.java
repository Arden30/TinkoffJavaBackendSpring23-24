package edu.java.bot.links;

import java.net.URI;

public abstract class LinkValidator {
    protected LinkValidator next;

    public static LinkValidator createChain(LinkValidator first, LinkValidator... links) {
        LinkValidator head = first;

        for (LinkValidator next : links) {
            head.next = next;
            head = next;
        }

        return first;
    }

    public abstract String getHostName();

    public boolean isValid(URI uri) {
        if (uri.getHost() != null && uri.getHost().equals(getHostName())) {
            return true;
        }

        if (next != null) {
            return next.isValid(uri);
        }

        return false;
    }
}
