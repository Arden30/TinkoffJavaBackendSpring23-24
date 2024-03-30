package edu.java.links.listener;

import edu.java.model.Link;

public interface LinkListener {
    void onLinkAdd(Link link);

    void onLinkRemove(Link link);
}
