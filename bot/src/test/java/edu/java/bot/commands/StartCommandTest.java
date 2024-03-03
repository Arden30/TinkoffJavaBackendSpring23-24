package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.services.CommandsService;
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
public class StartCommandTest {
    CommandsService commandsService;

    UserService userService;

    @Autowired
    StartCommandTest(UserService userService, CommandsService commandsService) {
        this.commandsService = commandsService;
        this.userService = userService;
    }
    @Mock
    Update update;

    @Mock
    Message message;

    @Mock
    Chat chat;

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
    @DisplayName("Command start test")
    void testStart() {
        String expected = "You were successfully registered!";
        SendMessage sendMessage = commandsService.getCommands().get("/start").handle(update);
        assertThat(sendMessage.toWebhookResponse()).contains(expected);

        String expected2 = "You are already registered";
        SendMessage sendMessage2 = commandsService.getCommands().get("/start").handle(update);
        assertThat(sendMessage2.toWebhookResponse()).contains(expected2);
    }
}
