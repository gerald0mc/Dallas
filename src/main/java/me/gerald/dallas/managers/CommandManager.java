package me.gerald.dallas.managers;

import me.gerald.dallas.features.command.Command;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static me.gerald.dallas.utils.ReflectionUtil.REFLECTIONS;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    public String PREFIX = "-";

    public CommandManager() {
        REFLECTIONS.getSubTypesOf(Command.class).forEach(command -> {
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
}
