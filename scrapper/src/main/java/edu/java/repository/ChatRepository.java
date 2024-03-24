package edu.java.repository;

import edu.java.model.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    List<Chat> findAll();

    List<Chat> findChatsByLinksId(long linkId);

    Optional<Chat> findById(long chatId);

    Chat addChat(Chat chat);

    boolean removeChatById(long chatId);
}
