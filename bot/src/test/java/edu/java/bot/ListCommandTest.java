package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.services.CommandsService;
import edu.java.bot.services.MessageService;
import edu.java.bot.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class ListCommandTest {
    CommandsService commandsService;
    MessageService messageService;
    UserService userService;

    @Autowired
    ListCommandTest(CommandsService commandsService, MessageService messageService, UserService userService) {
        this.commandsService = commandsService;
        this.messageService = messageService;
        this.userService = userService;
    }
    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    Chat chat;

    String link = "https://github.com/sanyarnd/java-course-2023-backend-template";

    @BeforeEach
    void setMock() {
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(1L);
        when(update.message().chat().id()).thenReturn(1L);
    }

    @AfterEach
    void clearDatabase() {
        userService.getUsers().clear();
    }

    @Test
    @DisplayName("Command list test")
    void testList() {
        userService.register(update.message().chat().id());
        messageService.addLink(update.message().chat().id(), link);

        String expectedLinks = "Your links: \\nhttps://github.com/sanyarnd/java-course-2023-backend-template\\n";
        SendMessage sendMessage = commandsService.getCommands().get("/list").handle(update);
        assertThat(sendMessage.toWebhookResponse()).contains(expectedLinks);

        messageService.deleteLink(update.message().chat().id(), link);

        String expectedEmptyList = "List is empty";
        SendMessage sendMessage2 = commandsService.getCommands().get("/list").handle(update);
        assertThat(sendMessage2.toWebhookResponse()).contains(expectedEmptyList);
    }
}
