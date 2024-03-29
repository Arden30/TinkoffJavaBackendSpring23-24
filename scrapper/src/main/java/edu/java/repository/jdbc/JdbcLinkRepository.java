package edu.java.repository.jdbc;

import edu.java.configuration.JdbcMappersConfig;
import edu.java.model.Link;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
    private final static String FIND_ALL = """
         SELECT link.* FROM link
         JOIN link_to_chat ON link.id = link_id
         WHERE chat_id = ?
        """;

    private final static String FIND_BY_ID = "SELECT * FROM link WHERE id = ?";
    private final static String FIND_BY_URL = "SELECT * FROM link WHERE url = ?";
    private final static String FIND_CHATS_BY_LINK = """
        SELECT chat.* FROM chat
        JOIN link_to_chat ON chat.id = chat_id
        WHERE link_id = ?
        """;
    private final static String ADD_LINK = "INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)";
    private final static String ADD_TO_CHAT = "INSERT INTO link_to_chat VALUES(?, ?)";
    private final static String DELETE_LINK = "DELETE FROM link WHERE id = ?";
    private final static String DELETE_LINK_IN_CHAT = "DELETE FROM link_to_chat WHERE (chat_id, link_id) = (?, ?)";
    private final static String EXISTS_IN_CHAT =
        "SELECT EXISTS(SELECT * FROM link_to_chat WHERE (chat_id, link_id) = (?, ?))";
    private final static String UPDATE = "UPDATE link SET updated_at = ? WHERE id = ?";
    private final static String RECENTLY_UPDATED = "SELECT * FROM link WHERE updated_at < ?";
    private final JdbcTemplate jdbcTemplate;

    private final JdbcMappersConfig jdbcMappersConfig;

    @Override
    public List<Link> findAllByChat(long chatId) {
        return jdbcTemplate.query(FIND_ALL, jdbcMappersConfig.linkMapper(), chatId);
    }

    @Override
    public Optional<Link> findById(long linkId) {
        return jdbcTemplate.query(FIND_BY_ID, jdbcMappersConfig.linkMapper(), linkId).stream().findFirst();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return jdbcTemplate.query(FIND_BY_URL, jdbcMappersConfig.linkMapper(), url).stream().findFirst();
    }

    @Override
    public boolean findLinkInAllChats(long linkId) {
        return jdbcTemplate.query(FIND_CHATS_BY_LINK, jdbcMappersConfig.linkMapper(), linkId).stream()
            .findFirst().isPresent();
    }

    @Override
    public Link addLink(Link link) {
        if (link.getId() == null) {
            jdbcTemplate.update(ADD_LINK, link.getUrl(), link.getCreatedAt(), link.getUpdatedAt());
            Long id = jdbcTemplate.query(FIND_BY_URL, jdbcMappersConfig.linkMapper(), link.getUrl())
                .stream()
                .findFirst()
                .get()
                .getId();
            link.setId(id);
        }

        return link;
    }

    @Override
    public boolean addLinkToChat(long chatId, long linkId) {
        if (Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXISTS_IN_CHAT, boolean.class, chatId, linkId))) {
            return false;
        }

        jdbcTemplate.update(ADD_TO_CHAT, chatId, linkId);
        return true;
    }

    @Override
    public boolean removeLink(long linkId) {
        return jdbcTemplate.update(DELETE_LINK, linkId) >= 1;
    }

    @Override
    public void saveChanges(Link link) {
        jdbcTemplate.update(UPDATE, link.getUpdatedAt(), link.getId());
    }

    @Override
    public boolean removeLinkByChat(long chatId, long linkId) {
        return jdbcTemplate.update(DELETE_LINK_IN_CHAT, chatId, linkId) >= 1;
    }

    @Override
    public List<Link> recentlyUpdated(OffsetDateTime oldThan) {
        return jdbcTemplate.query(RECENTLY_UPDATED, new BeanPropertyRowMapper<>(Link.class), oldThan);
    }
}
