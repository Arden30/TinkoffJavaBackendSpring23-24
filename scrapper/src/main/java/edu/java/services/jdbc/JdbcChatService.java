package edu.java.services.jdbc;

import edu.java.api.exceptions.DoubleRegistrationException;
import edu.java.api.exceptions.NoSuchChatException;
import edu.java.model.Chat;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.services.ChatService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository jdbcChatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        if (jdbcChatRepository.findById(tgChatId).isPresent()) {
            throw new DoubleRegistrationException("Chat with this id " + tgChatId + " is already registered!");
        }

        jdbcChatRepository.addChat(chat);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        jdbcChatRepository.findById(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("Chat with id " + tgChatId + " is not registered!"));
        jdbcChatRepository.removeChatById(tgChatId);
    }
}
