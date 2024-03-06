package edu.java.services;

import edu.java.api.dto.response.LinkResponse;
import edu.java.api.dto.response.ListLinksResponse;
import edu.java.api.exceptions.DoubleLinkException;
import edu.java.api.exceptions.DoubleRegistrationException;
import edu.java.api.exceptions.NoSuchLinkException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Map<Long, List<LinkResponse>> chatLinks = new HashMap<>();

    public void checkChatExistence(long id) {
        if (chatLinks.containsKey(id)) {
            throw new DoubleRegistrationException("Chat with id " + id + " is already registered");
        }
    }

    public void registerChat(long id) {
        checkChatExistence(id);

        chatLinks.put(id, new ArrayList<>());
    }

    public void deleteChat(long id) {
        checkChatExistence(id);

        chatLinks.remove(id);
    }

    public ListLinksResponse getLinks(long id) {
        checkChatExistence(id);

        return new ListLinksResponse(chatLinks.get(id));
    }

    public LinkResponse addLink(long id, URI link) {
        checkChatExistence(id);

        List<LinkResponse> linkResponses = chatLinks.get(id);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                throw new DoubleLinkException("Link " + link.getPath() + " is already being tracked");
            }
        }

        LinkResponse linkResponse = new LinkResponse(linkResponses.size() + 1, link);
        linkResponses.add(linkResponse);

        return linkResponse;
    }

    public LinkResponse removeLink(long id, URI link) {
        checkChatExistence(id);

        List<LinkResponse> linkResponses = chatLinks.get(id);
        for (LinkResponse linkResponse : linkResponses) {
            if (linkResponse.url().getPath().equals(link.getPath())) {
                linkResponses.remove(linkResponse);
                return linkResponse;
            }
        }

        throw new NoSuchLinkException("This Link " + link.getPath() + " is not being tracked");
    }
}
