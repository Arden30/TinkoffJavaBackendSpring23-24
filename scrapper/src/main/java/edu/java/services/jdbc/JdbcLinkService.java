package edu.java.services.jdbc;

import edu.java.api.exceptions.DoubleLinkException;
import edu.java.api.exceptions.NoSuchLinkException;
import edu.java.links.listener.LinkListener;
import edu.java.model.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final List<LinkListener> linkListeners;
    private final JdbcLinkRepository jdbcLinkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, String url) {
        Link newLink = new Link();
        newLink.setCreatedAt(OffsetDateTime.now());
        newLink.setUpdatedAt(OffsetDateTime.now());
        newLink.setUrl(url);

        Link link = jdbcLinkRepository.findByUrl(url).orElseGet(() -> jdbcLinkRepository.addLink(newLink));
        if (!jdbcLinkRepository.addLinkToChat(tgChatId, link.getId())) {
            throw new DoubleLinkException("Link is already being tracked");
        }

        linkListeners.forEach(listener -> listener.onLinkAdd(link));

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, String url) {
        Link link = jdbcLinkRepository.findByUrl(url).orElseThrow(() -> new NoSuchLinkException("Link was not found"));
        if (!jdbcLinkRepository.removeLinkByChat(tgChatId, link.getId())) {
            throw new NoSuchLinkException("No such link");
        }

        if (!jdbcLinkRepository.findLinkInAllChats(link.getId())) {
            jdbcLinkRepository.removeLink(link.getId());
            linkListeners.forEach(linkListener -> linkListener.onLinkRemove(link));
        }

        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return jdbcLinkRepository.findAllByChat(tgChatId);
    }
}
