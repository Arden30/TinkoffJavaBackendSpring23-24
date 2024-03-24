package edu.java.scrapper.integration_tests;

import edu.java.model.Chat;
import edu.java.model.Link;
import edu.java.repository.jdbc.JdbcChatRepository;
import edu.java.repository.jdbc.JdbcLinkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Rollback
public class JdbcRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final Link link = new Link();
    private final Link link2 = new Link();
    private final Chat chat = new Chat();
    private final String url = "https://github.com/Arden30/TinkoffJavaBackendSpring23-24";
    private final String url2 = "https://github.com/sanyarnd/java-course-2023-backend-template";

    @BeforeEach
    void setUp() {
        link.setUrl(url);
        link.setCreatedAt(OffsetDateTime.now());
        link.setUpdatedAt(OffsetDateTime.now());

        link2.setUrl(url2);
        link2.setCreatedAt(OffsetDateTime.now());
        link2.setUpdatedAt(OffsetDateTime.now());

        chat.setId(1L);
        chat.setCreatedAt(OffsetDateTime.now());
    }

    @BeforeEach
    @AfterEach
    public void restartIdentity() {
        jdbcTemplate.update("TRUNCATE link_to_chat RESTART IDENTITY");
        jdbcTemplate.update("TRUNCATE chat RESTART IDENTITY CASCADE");
        jdbcTemplate.update("TRUNCATE link RESTART IDENTITY CASCADE");
    }

    @Test
    void addAndFindLinkTest() {
        chatRepository.addChat(chat);
        linkRepository.addLink(link);
        linkRepository.addLinkToChat(chat.getId(), link.getId());

        assertThat(linkRepository.findById(link.getId()).get().getUrl()).isEqualTo(url);
        assertThat(linkRepository.findLinksByChatsId(chat.getId()).get(0).getUrl()).isEqualTo(url);
    }

    @Test
    void removeAndFindLinkTest() {
        jdbcTemplate.update("INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)", link.getUrl(), link.getCreatedAt(), link.getUpdatedAt());
        boolean res = linkRepository.removeLink(1L);

        assertThat(res).isTrue();
        assertThat(linkRepository.findByUrl(link.getUrl())).isEmpty();
    }

    @Test
    void addChatAndFindTest() {
        chatRepository.addChat(chat);

        assertThat(chatRepository.findById(chat.getId())).isPresent();
    }

    @Test
    void removeChatAndFindTest() {
        jdbcTemplate.update("INSERT INTO chat(id, created_at) VALUES (?, ?)", chat.getId(), chat.getCreatedAt());
        boolean res = chatRepository.removeChatById(chat.getId());

        assertThat(res).isTrue();
        assertThat(chatRepository.findById(chat.getId())).isEmpty();
    }

    @Test
    void findAllByChatTest() {
        jdbcTemplate.update("INSERT INTO chat(id, created_at) VALUES (?, ?)", chat.getId(), chat.getCreatedAt());

        jdbcTemplate.update("INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)", url, link.getCreatedAt(), link.getUpdatedAt());
        jdbcTemplate.update("INSERT INTO link_to_chat VALUES (?, ?)", chat.getId(), 1L);

        jdbcTemplate.update("INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)", url2, link.getCreatedAt(), link.getUpdatedAt());
        jdbcTemplate.update("INSERT INTO link_to_chat VALUES (?, ?)", chat.getId(), 2L);

        List<Link> links = linkRepository.findLinksByChatsId(chat.getId());
        assertThat(links.get(0).getUrl()).isEqualTo(url);
        assertThat(links.get(1).getUrl()).isEqualTo(url2);
    }

    @Test
    void removeLinkByChatTest() {
        jdbcTemplate.update("INSERT INTO chat(id, created_at) VALUES (?, ?)", chat.getId(), chat.getCreatedAt());

        jdbcTemplate.update("INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)", url, link.getCreatedAt(), link.getUpdatedAt());
        jdbcTemplate.update("INSERT INTO link_to_chat VALUES (?, ?)", chat.getId(), 1L);

        jdbcTemplate.update("INSERT INTO link(url, created_at, updated_at) VALUES (?, ?, ?)", url2, link.getCreatedAt(), link.getUpdatedAt());
        jdbcTemplate.update("INSERT INTO link_to_chat VALUES (?, ?)", chat.getId(), 2L);

        boolean res = linkRepository.removeLinkByChat(chat.getId(), 1L);

        List<Link> links = linkRepository.findLinksByChatsId(chat.getId());

        assertThat(res).isTrue();
        assertThat(links.size()).isEqualTo(1);
        assertThat(links.get(0).getUrl()).isEqualTo(url2);
    }
}
