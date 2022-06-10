package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

import java.io.IOException;

public class Config extends Command {
    public Config() {
        super("Config", "Allows you to load and save custom configs.", new String[]{"config", "[<save> <name>/<load> <name>]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length < 2) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Config", "Please specify saving or loading of configs.", MessageUtil.MessageType.CONSTANT);
            return;
        } else if (args.length < 3) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Config", "Please specify the name of the config.", MessageUtil.MessageType.CONSTANT);
            return;
        }
        String configName = args[2];
        switch (args[1]) {
            case "save":
                try {
                    ConfigManager.save(configName);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Config", "Saved config under name " + ChatFormatting.AQUA + configName + ChatFormatting.RESET + "!", MessageUtil.MessageType.CONSTANT);
                } catch (IOException ignored) {
                }
                break;
            case "load":
                ConfigManager.load(configName);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Config", "Loaded config under name " + ChatFormatting.AQUA + configName + ChatFormatting.RESET + "!", MessageUtil.MessageType.CONSTANT);
                break;
        }
    }
}
