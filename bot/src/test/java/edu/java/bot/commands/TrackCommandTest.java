//package edu.java.bot.commands;
//
//import com.pengrad.telegrambot.model.Chat;
//import com.pengrad.telegrambot.model.Message;
//import com.pengrad.telegrambot.model.Update;
//import com.pengrad.telegrambot.request.SendMessage;
//import edu.java.bot.BotApplication;
//import edu.java.bot.services.CommandsService;
//import edu.java.bot.services.LinkService;
//import edu.java.bot.services.UserService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = {BotApplication.class})
//public class TrackCommandTest {
//    CommandsService commandsService;
//    LinkService linkService;
//    UserService userService;
//
//    @Autowired
//    TrackCommandTest(CommandsService commandsService, LinkService linkService, UserService userService) {
//        this.commandsService = commandsService;
//        this.linkService = linkService;
//        this.userService = userService;
//    }
//    @Mock
//    Update update;
//
//    @Mock
//    Message message;
//
//    @Mock
//    Chat chat;
//
//    String link = "https://github.com/sanyarnd/java-course-2023-backend-template";
//
//    @BeforeEach
//    void setMock() {
//        when(update.message()).thenReturn(message);
//        when(message.chat()).thenReturn(chat);
//        when(chat.id()).thenReturn(1L);
//        when(update.message().chat().id()).thenReturn(1L);
//    }
//
//    @AfterEach
//    void clearDatabase() {
//        userService.getUsers().clear();
//    }
//
//    @Test
//    @DisplayName("Command track test")
//    void testTrack() {
//        userService.register(update.message().chat().id());
//
//        String expectedInput = "Input your link to track (github/stackoverflow)";
//        SendMessage sendMessage = commandsService.getCommands().get("/track").handle(update);
//        assertThat(sendMessage.toWebhookResponse()).contains(expectedInput);
//
//        String expectedLink = "Now your link is being tracked:\\n" + link;
//        SendMessage linkResponse = linkService.addLink(update.message().chat().id(), link);
//        assertThat(linkResponse.toWebhookResponse()).contains(expectedLink);
//    }
//}
