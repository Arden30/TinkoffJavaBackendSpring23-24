package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.Command;
import edu.java.bot.services.CommandsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BotConfig {
    private final ApplicationConfig applicationConfig;
    private final CommandsService commandsService;

    @Bean
    public Map<String, Command> commands() {
        commandsService.setAllCommands();
        return commandsService.getCommands();
    }

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot telegramBot = new TelegramBot(applicationConfig.telegramToken());
        telegramBot.execute(createCommandMenu());
        return telegramBot;
    }

    private SetMyCommands createCommandMenu() {
        Map<String, Command> commands = commands();

        return new SetMyCommands(commands.values().stream().map(command -> new BotCommand(command
                .command(), command.description()))
            .toArray(BotCommand[]::new));
    }
}
