package me.gerald.dallas.command;

import me.gerald.dallas.command.commands.Friend;
import me.gerald.dallas.command.commands.Help;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commands;
    public String PREFIX = "-";

    public CommandManager() {
        commands = new ArrayList<>();
        commands.add(new Help());
        commands.add(new Friend());
    }

    public List<Command> getCommands() {
        return commands;
    }
}
