package edu.java.services.jooq;

import edu.java.api.exceptions.DoubleLinkException;
import edu.java.api.exceptions.NoSuchLinkException;
import edu.java.model.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository jooqLinkRepository;

    @Override
    @Transactional
    public Link add(long tgChatId, String url) {
        Link newLink = new Link();
        newLink.setCreatedAt(OffsetDateTime.now());
        newLink.setUpdatedAt(OffsetDateTime.now());
        newLink.setUrl(url);

        Link link = jooqLinkRepository.findByUrl(url).orElseGet(() -> jooqLinkRepository.addLink(newLink));

        if (!jooqLinkRepository.addLinkToChat(tgChatId, link.getId())) {
            throw new DoubleLinkException("Link is already being tracked");
        }

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, String url) {
        Link link = jooqLinkRepository.findByUrl(url).orElseThrow(() -> new NoSuchLinkException("Link was not found"));
        jooqLinkRepository.removeLinkByChat(tgChatId, link.getId());

        if (!jooqLinkRepository.findLinkInAllChats(link.getId())) {
            jooqLinkRepository.removeLink(link.getId());
        }

        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return jooqLinkRepository.findAllByChat(tgChatId);
    }
}
