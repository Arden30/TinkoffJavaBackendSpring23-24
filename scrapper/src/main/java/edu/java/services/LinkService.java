package edu.java.services;

import edu.java.model.Link;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, String url);

    Link remove(long tgChatId, String url);

    List<Link> listAll(long tgChatId);
}
