package edu.java.services.jooq;

import edu.java.api.exceptions.NoSuchLinkException;
import edu.java.links.listener.LinkListener;
import edu.java.model.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final List<LinkListener> linkListeners;
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, String url) {
        Link newLink = new Link();
        newLink.setCreatedAt(OffsetDateTime.now());
        newLink.setUpdatedAt(OffsetDateTime.now());
        newLink.setUrl(url);

        Link link = jooqLinkRepository.findByUrl(url).orElseGet(() -> jooqLinkRepository.addLink(newLink));
        jooqLinkRepository.addLinkToChat(tgChatId, link.getId());

        linkListeners.forEach(listener -> listener.onLinkAdd(link));

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, String url) {
        Link link = jooqLinkRepository.findByUrl(url).orElseThrow(() -> new NoSuchLinkException("Link was not found"));
        jooqLinkRepository.removeLinkByChat(tgChatId, link.getId());

        if (!jooqLinkRepository.findLinkInAllChats(link.getId())) {
            jooqLinkRepository.removeLink(link.getId());
            linkListeners.forEach(linkListener -> linkListener.onLinkRemove(link));
        }

        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return jooqLinkRepository.findAllByChat(tgChatId);
    }
}
