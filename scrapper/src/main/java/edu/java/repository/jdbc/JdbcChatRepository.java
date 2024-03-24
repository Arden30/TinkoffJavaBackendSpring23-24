package edu.java.repository.jdbc;

import edu.java.configuration.JdbcMappersConfig;
import edu.java.model.Chat;
import edu.java.repository.ChatRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
    private final JdbcMappersConfig jdbcMappersConfig;
    private final static String FIND_ALL = "SELECT * FROM chat";
    private final static String FIND_ALL_BY_LINK = """
            SELECT chat.* FROM chat
            JOIN link_to_chat ON chat.id = chat_id
            WHERE link_id = ?
            """;
    private final static String FIND_BY_ID = "SELECT * FROM chat WHERE id = ?";
    private final static String ADD_CHAT = "INSERT INTO chat(id, created_at) VALUES (?, ?)";
    private final static String DELETE_CHAT = "DELETE FROM chat WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Chat> findAll() {
        return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Chat.class));
    }

    @Override
    public List<Chat> findChatsByLinksId(long linkId) {
        return jdbcTemplate.query(FIND_ALL_BY_LINK, jdbcMappersConfig.chatMapper(), linkId);
    }

    @Override
    public Optional<Chat> findById(long id) {
        return jdbcTemplate.query(FIND_BY_ID, jdbcMappersConfig.chatMapper(), id).stream().findFirst();
    }

    @Override
    public Chat addChat(Chat chat) {
        jdbcTemplate.update(ADD_CHAT, chat.getId(), chat.getCreatedAt());

        return chat;
    }

    @Override
    public boolean removeChatById(long id) {
        return jdbcTemplate.update(DELETE_CHAT, id) >= 1;
    }
}
