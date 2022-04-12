package me.gerald.dallas.features.command;

import java.io.IOException;

public class Command {
    private String name, description;
    private String[] usage;

    public Command(String name, String description, String[] usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public void onCommand(String[] args) {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[] getUsage() {
        return usage;
    }
}
