package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.features.modules.misc.Emojis;
import me.gerald.dallas.utils.MessageUtil;

import java.util.Map;

public class Emoji extends Command {
    public Emoji() {
        super("Emoji", "Emoji command.", new String[]{"emoji", "[add/list]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", "Please specify which sub command you would like to use.", true);
            return;
        }
        switch (args[1]) {
            case "add":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", "Please specify the name and emoji you would like to add.", true);
                    return;
                }
                String name = args[2];
                if (args.length == 3) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", "Please specify the emoji you would like to add.", true);
                    return;
                }
                String emoji = args[3];
                Yeehaw.INSTANCE.moduleManager.getModule(Emojis.class).emojis.put(name, emoji);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", "Added new emoji to list called " + name + " and is set to " + emoji, true);
                break;
            case "list":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", ChatFormatting.AQUA + "Emoji " + ChatFormatting.RESET + "List", true);
                for (Map.Entry<String, String> entry : Yeehaw.INSTANCE.moduleManager.getModule(Emojis.class).emojis.entrySet()) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji", ChatFormatting.AQUA + entry.getKey() + " " + ChatFormatting.GREEN + entry.getValue(), true);
                }
                break;
        }
    }
}
