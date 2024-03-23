package edu.java.services.jooq;

import edu.java.api.exceptions.DoubleRegistrationException;
import edu.java.api.exceptions.NoSuchChatException;
import edu.java.model.Chat;
import edu.java.repository.jooq.JooqChatRepository;
import edu.java.services.ChatService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqChatRepository jooqChatRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        Chat chat = new Chat();
        chat.setId(tgChatId);
        chat.setCreatedAt(OffsetDateTime.now());

        if (jooqChatRepository.findById(tgChatId).isPresent()) {
            throw new DoubleRegistrationException("Chat with this id " + tgChatId + " is already registered!");
        }

        jooqChatRepository.addChat(chat);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        jooqChatRepository.findById(tgChatId)
            .orElseThrow(() -> new NoSuchChatException("Chat with id " + tgChatId + " is not registered!"));
        jooqChatRepository.removeChatById(tgChatId);
    }
}
