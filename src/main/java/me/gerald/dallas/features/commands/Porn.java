package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Porn extends Command {
    public Porn() {
        super("Porn", "Does porn things.", new String[] {"porn", "<type>"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if(args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Porn", "Please specify the type of porn you wish to see.", true);
            return;
        }
        String type = "";
        if(args.length == 2) {
            type = args[1];
        } else {
            for(int i = 1; i < args.length; i++) {
                if(i + 1 == args.length + 1) {
                    type += args[i];
                    break;
                } else {
                    type += args[i] + "+";
                }
            }
        }
        try {
            Desktop.getDesktop().browse(URI.create("https://www.pornhub.com/video/search?search=" + type));
        } catch (IOException ignored) { }
    }
}
