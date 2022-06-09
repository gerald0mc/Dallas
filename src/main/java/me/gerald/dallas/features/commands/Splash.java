package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

public class Splash extends Command {
    public Splash() {
        super("Splash", "Add and removes messages from the splash menu.", new String[]{"splash", "[<add> <text>/<del> <index>/<list>]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", "Please specify which subcommand you would like to use.", true);
            return;
        }
        switch (args[1]) {
            case "list":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", ChatFormatting.GOLD + "Splash " + ChatFormatting.RESET + "List" + ChatFormatting.GRAY + ":", true);
                for (String s : Yeehaw.INSTANCE.splashText) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", s, true);
                }
                return;
            case "add":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", "Please specify the splash you wish to add to the list.", true);
                    return;
                }
                String splash = "";
                for (int i = 2; i < args.length; i++) {
                    if (i == 2) {
                        splash += args[i];
                    } else
                        splash += " " + args[i];
                }
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", "Adding Splash " + ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + splash + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "to Splash Text List.", true);
                Yeehaw.INSTANCE.splashText.add(splash);
                return;
            case "del":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", "Please specify the integer of the splash you wish to delete. (EX 0-5)", true);
                    return;
                }
                int targetInt = Integer.parseInt(args[2]);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Splash", "Removing Splash " + ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + Yeehaw.INSTANCE.splashText.get(targetInt) + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + "from Splash List.", true);
                Yeehaw.INSTANCE.splashText.remove(targetInt);
        }
    }
}
