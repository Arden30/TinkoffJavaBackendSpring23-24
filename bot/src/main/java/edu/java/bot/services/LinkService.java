package edu.java.bot.services;

import edu.java.bot.client.ScrapperWebClient;
import edu.java.bot.client.dto.request.AddLinkRequest;
import edu.java.bot.client.dto.request.RemoveLinkRequest;
import edu.java.bot.client.dto.response.LinkResponse;
import edu.java.bot.client.dto.response.ListLinksResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final ScrapperWebClient scrapperWebClient;

    public Optional<LinkResponse> addLink(long id, AddLinkRequest addLinkRequest) {
        return scrapperWebClient.addLink(id, addLinkRequest);
    }

    public Optional<LinkResponse> deleteLink(long id, RemoveLinkRequest request) {
        return scrapperWebClient.removeLink(id, request);
    }

    public Optional<ListLinksResponse> getLinks(long id) {
        return scrapperWebClient.getLinks(id);
    }
}
