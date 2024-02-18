package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.model.State;
import edu.java.bot.model.User;
import edu.java.bot.services.CommandsService;
import edu.java.bot.services.MessageService;
import edu.java.bot.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BotConfig implements UpdatesListener, AutoCloseable {
    private final ApplicationConfig applicationConfig;
    private final UserService userService;
    private final MessageService messageService;
    private final CommandsService commandsService;
    private Map<String, Command> commands;
    private TelegramBot telegramBot;

    @Bean
    public Map<String, Command> setCommands() {
        commandsService.setAllCommands();
        commands = commandsService.getCommands();
        return commands;
    }

    @Bean
    public TelegramBot start() {
        telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.setUpdatesListener(this);
        telegramBot.execute(createCommandMenu());
        return telegramBot;
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Long id = update.message().chat().id();
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isPresent()) {
                State state = userService.getUsers().get(id).getState();

                if (state.equals(State.DEFAULT) && commands.containsKey(update.message().text())) {
                    telegramBot.execute(commands.get(update.message().text()).handle(update));
                } else if (state.equals(State.ADD_LINK)) {
                    telegramBot.execute(messageService.addLink(id, update.message().text()));
                } else if (state.equals(State.DELETE_LINK)) {
                    telegramBot.execute(messageService.deleteLink(id, update.message().text()));
                } else {
                    telegramBot.execute(new SendMessage(id, "No such command"));
                }
            } else {
                if (commands.containsKey(update.message().text())) {
                    telegramBot.execute(commands.get(update.message().text()).handle(update));
                }
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void close() {
        telegramBot.shutdown();
    }

    private SetMyCommands createCommandMenu() {
        return new SetMyCommands(commands.values().stream().map(command -> new BotCommand(command
                .command(), command.description()))
            .toArray(BotCommand[]::new));
    }
}
