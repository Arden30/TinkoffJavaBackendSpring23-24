package edu.java.bot.services;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class CommandsService {
    private final UserService userService;
    private final LinkService linkService;
    private final Map<String, Command> commands = new HashMap<>();

    @Autowired
    public CommandsService(UserService userService, LinkService linkService, List<Command> commands) {
        this.userService = userService;
        this.linkService = linkService;
        commands.forEach(command -> this.commands.put(command.command(), command));
    }

    public void setAllCommands() {
        Command help = new HelpCommand(this);
        Command list = new ListCommand(linkService);
        Command track = new TrackCommand();
        Command untrack = new UntrackCommand();
        Command start = new StartCommand(userService);

        this.commands.put(help.command(), help);
        this.commands.put(list.command(), list);
        this.commands.put(track.command(), track);
        this.commands.put(untrack.command(), untrack);
        this.commands.put(start.command(), start);
    }
}
