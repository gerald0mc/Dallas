package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ConsoleMessageEvent;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.module.misc.Emojis;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class Emoji extends Command {
    public Emoji() {
        super("Emoji", "Emoji command.", new String[] {"emoji", "[add/list]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if(args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji Command", "Please specify which sub command you would like to use.", true);
            MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify which sub command you would like to use."));
            return;
        }
        switch (args[1]) {
            case "add":
                if(args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji Command", "Please specify the name and emoji you would like to add.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify the name and emoji you would like to add."));
                    return;
                }
                String name = args[2];
                if(args.length == 3) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji Command", "Please specify the emoji you would like to add.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify the emoji you would like to add."));
                    return;
                }
                String emoji = args[3];
                Yeehaw.INSTANCE.moduleManager.getModule(Emojis.class).emojis.put(name, emoji);
                break;
            case "list":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji Command", ChatFormatting.AQUA + "Emoji " + ChatFormatting.RESET + "List", true);
                MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(ChatFormatting.AQUA + "Emoji " + ChatFormatting.RESET + "List"));
                for(Map.Entry<String, String> entry : Yeehaw.INSTANCE.moduleManager.getModule(Emojis.class).emojis.entrySet()) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Emoji Command", ChatFormatting.AQUA + entry.getKey() + " " + ChatFormatting.GREEN + entry.getValue(), true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(ChatFormatting.AQUA + entry.getKey() + " " + ChatFormatting.GREEN + entry.getValue()));
                }
                break;
        }
    }
}
