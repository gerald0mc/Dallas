package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

import java.awt.*;
import java.io.IOException;

public class Explorer extends Command {
    public Explorer() {
        super("Explorer", "Opens the clients config folder.", new String[]{"explorer"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        try {
            Desktop.getDesktop().open(ConfigManager.mainPath);
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Explorer", "Opened the client path in explorer.", MessageUtil.MessageType.CONSTANT);
        } catch (IOException ignored) {
        }
    }
}
