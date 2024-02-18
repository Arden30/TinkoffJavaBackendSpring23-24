package edu.java.bot.services;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class CommandsService {
    private final UserService userService;
    private final Map<String, Command> commands = new HashMap<>();

    public void setAllCommands() {
        Command help = new HelpCommand(this);
        Command list = new ListCommand(userService);
        Command track = new TrackCommand(userService);
        Command untrack = new UntrackCommand(userService);
        Command start = new StartCommand(userService);

        commands.put(help.command(), help);
        commands.put(list.command(), list);
        commands.put(track.command(), track);
        commands.put(untrack.command(), untrack);
        commands.put(start.command(), start);
    }
}
