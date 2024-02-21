package edu.java.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.model.State;
import edu.java.bot.model.User;
import edu.java.bot.services.MessageService;
import edu.java.bot.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotListener implements UpdatesListener, AutoCloseable {
    private final Map<String, Command> commands;
    private final TelegramBot telegramBot;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public BotListener(
        Map<String, Command> commands,
        TelegramBot telegramBot,
        UserService userService,
        MessageService messageService
    ) {
        this.commands = commands;
        this.telegramBot = telegramBot;
        this.userService = userService;
        this.messageService = messageService;
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Long id = update.message().chat().id();
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isPresent()) {
                State state = userService.getUsers().get(id).getState();

                switch (state) {
                    case ADD_LINK -> telegramBot.execute(messageService.addLink(id, update.message().text()));
                    case DELETE_LINK -> telegramBot.execute(messageService.deleteLink(id, update.message().text()));
                    default -> {
                        if (commands.containsKey(update.message().text())) {
                            telegramBot.execute(commands.get(update.message().text()).handle(update));
                        } else {
                            telegramBot.execute(new SendMessage(id, "No such command"));
                        }
                    }
                }

            } else {
                if (commands.containsKey(update.message().text())) {
                    telegramBot.execute(commands.get(update.message().text()).handle(update));
                }
            }
        }

        return com.pengrad.telegrambot.UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }
}
