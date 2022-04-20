package me.gerald.dallas.features.command;

import net.minecraftforge.common.MinecraftForge;

public class Command {
    private final String name;
    private final String description;
    private final String[] usage;

    public Command(String name, String description, String[] usage) {
        MinecraftForge.EVENT_BUS.register(this);
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public void onCommand(String[] args) {
    }

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
