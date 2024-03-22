package edu.java.repository.jooq;

import edu.java.model.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Chat.CHAT;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.Link.LINK;
import static ru.tinkoff.edu.java.scrapper.domain.jooq.tables.LinkToChat.LINK_TO_CHAT;

@Primary
@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dsl;

    @Override
    public List<Link> findAllByChat(long chatId) {
        return dsl.select(LINK.fields())
            .from(LINK)
            .join(LINK_TO_CHAT)
            .on(LINK.ID.eq(LINK_TO_CHAT.LINK_ID))
            .where(LINK_TO_CHAT.CHAT_ID.eq(chatId))
            .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return dsl.select(LINK)
            .from(LINK)
            .where(LINK.ID.eq(linkId))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dsl.select(LINK)
            .from(LINK)
            .where(LINK.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Link addLink(Link link) {
        if (link.getId() == null) {
            Long id = dsl.insertInto(LINK, LINK.URL, LINK.CREATED_AT, LINK.UPDATED_AT)
                .values(link.getUrl(), link.getCreatedAt(), link.getUpdatedAt())
                .returning(LINK.ID)
                .fetchInto(Long.class)
                .get(0);
            link.setId(id);
        }

        return link;
    }

    @Override
    public void saveChanges(Link link) {
        dsl.update(LINK)
            .set(LINK.UPDATED_AT, link.getUpdatedAt())
            .where(LINK.ID.eq(link.getId()));
    }

    @Override
    public boolean addLinkToChat(long chatId, long linkId) {
        boolean exists = dsl.selectFrom(LINK_TO_CHAT)
            .where(LINK_TO_CHAT.CHAT_ID.eq(chatId), LINK_TO_CHAT.LINK_ID.eq(linkId))
            .fetchOptional().isPresent();

        return !exists && dsl.insertInto(LINK_TO_CHAT, LINK_TO_CHAT.fields())
            .values(chatId, linkId)
            .execute() == 1;
    }

    @Override
    public boolean removeLink(long linkId) {
        return dsl.deleteFrom(LINK)
            .where(LINK.ID.eq(linkId))
            .execute() == 1;
    }

    @Override
    public boolean findLinkInAllChats(long linkId) {
        return dsl.select(CHAT.fields())
            .from(CHAT)
            .join(LINK_TO_CHAT)
            .on(CHAT.ID.eq(LINK_TO_CHAT.CHAT_ID))
            .where(LINK_TO_CHAT.LINK_ID.eq(linkId))
            .fetchInto(Link.class)
            .stream()
            .findFirst().isPresent();
    }

    @Override
    public boolean removeLinkByChat(long chatId, long linkId) {
        return dsl.deleteFrom(LINK_TO_CHAT)
            .where(LINK_TO_CHAT.CHAT_ID.eq(chatId), LINK_TO_CHAT.LINK_ID.eq(linkId))
            .execute() == 1;
    }

    @Override
    public List<Link> recentlyUpdated(OffsetDateTime oldThan) {
        return dsl.selectFrom(LINK)
            .where(LINK.UPDATED_AT.lessThan(oldThan))
            .fetchInto(Link.class);
    }
}
