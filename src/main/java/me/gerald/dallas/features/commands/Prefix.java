package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

public class Prefix extends Command {
    public Prefix() {
        super("Prefix", "Allows you to change the current client prefix.", new String[] {"prefix", "<value>"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if(args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Prefix", "Please enter a value for the new prefix.", true);
            return;
        } else if(args.length > 2 || args[1].length() > 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Prefix", "Please only try to set one letter as your new prefix.", true);
            return;
        }
        String prefix = args[1];
        Yeehaw.INSTANCE.commandManager.setPREFIX(prefix);
    }
}
