package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.BotApplication;
import edu.java.bot.services.CommandsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
public class HelpCommandTest {
    CommandsService commandsService;

    @Autowired
    HelpCommandTest(CommandsService commandsService) {
        this.commandsService = commandsService;
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

    @Test
    @DisplayName("Command help test")
    void testHelp() {
        String expected = "Available commands: \\n/list: shows links which are tracked\\n/help: list of available commands for bot\\n/track: starts to track updates on the site by your link\\n/start: register in WebSitesTracker\\n/untrack: deletes link from list of tracked\\n";

        SendMessage sendMessage = commandsService.getCommands().get("/help").handle(update);

        assertThat(sendMessage.toWebhookResponse()).contains(expected);
    }
}
