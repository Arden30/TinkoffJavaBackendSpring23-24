package edu.java.services.jdbc;

import edu.java.api.exceptions.DoubleLinkException;
import edu.java.api.exceptions.NoSuchLinkException;
import edu.java.model.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
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

        return link;
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, String url) {
        Link link = jdbcLinkRepository.findByUrl(url).orElseThrow(() -> new NoSuchLinkException("Link was not found"));
        jdbcLinkRepository.removeLinkByChat(tgChatId, link.getId());

        if (!jdbcLinkRepository.findLinkInAllChats(link.getId())) {
            jdbcLinkRepository.removeLink(link.getId());
        }

        return link;
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        return jdbcLinkRepository.findAllByChat(tgChatId);
    }
}
