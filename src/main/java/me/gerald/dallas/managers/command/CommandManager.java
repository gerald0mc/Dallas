package me.gerald.dallas.managers.command;

import me.gerald.dallas.utils.ReflectionUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    public String PREFIX = "-";

    public CommandManager() {
        ReflectionUtil.getSubclasses(Command.class).forEach(command -> {
            if (!Modifier.isAbstract(command.getModifiers())) {
                try {
                    commands.add(command.getDeclaredConstructor().newInstance());
                } catch (Exception exception) {
                    // this only fails if the constructor throws an exception or has parameters
                    // rethrow because it shouldn't happen
                    throw new RuntimeException("Could not initialize " + command, exception);
                }
            }
        });
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void setPREFIX(String PREFIX) {
        this.PREFIX = PREFIX;
    }
}
