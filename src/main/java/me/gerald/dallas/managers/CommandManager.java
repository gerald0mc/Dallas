package me.gerald.dallas.managers;

import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.command.impl.Friend;
import me.gerald.dallas.features.command.impl.Help;

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
