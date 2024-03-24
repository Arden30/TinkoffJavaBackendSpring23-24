package edu.java.repository;

import edu.java.model.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    List<Link> findAllByChat(long chatId);

    Optional<Link> findById(long linkId);

    Optional<Link> findByUrl(String url);

    Link addLink(Link link);

    void saveChanges(Link link);

    boolean addLinkToChat(long chatId, long linkId);

    boolean removeLink(long linkId);

    boolean findLinkInAllChats(long linkId);

    boolean removeLinkByChat(long chatId, long linkId);

    List<Link> recentlyUpdated(OffsetDateTime oldThan);
}
