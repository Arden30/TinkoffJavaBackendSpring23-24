package edu.java.repository.jooq;

import edu.java.model.Chat;
import edu.java.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.Chat.CHAT;
import static edu.java.domain.jooq.tables.LinkToChat.LINK_TO_CHAT;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    @Override
    public List<Chat> findAll() {
        return dsl.select(CHAT.fields())
            .from(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public List<Chat> findAllByLink(long linkId) {
        return dsl.select(CHAT.fields())
            .from(CHAT)
            .join(LINK_TO_CHAT)
            .on(CHAT.ID.eq(LINK_TO_CHAT.CHAT_ID))
            .where(LINK_TO_CHAT.LINK_ID.eq(linkId))
            .fetchInto(Chat.class);
    }

    @Override
    public Optional<Chat> findById(long chatId) {
        return dsl.select(CHAT.fields())
            .from(CHAT)
            .where(CHAT.ID.eq(chatId))
            .fetchOptionalInto(Chat.class);
    }

    @Override
    public Chat addChat(Chat chat) {
        return dsl.insertInto(CHAT, CHAT.ID, CHAT.CREATED_AT)
            .values(chat.getId(), chat.getCreatedAt())
            .returning(CHAT.fields())
            .fetchAnyInto(Chat.class);
    }

    @Override
    public boolean removeChatById(long chatId) {
        return dsl.deleteFrom(CHAT)
            .where(CHAT.ID.eq(chatId))
            .execute() == 1;
    }
}
