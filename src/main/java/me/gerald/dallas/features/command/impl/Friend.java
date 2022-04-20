package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ConsoleMessageEvent;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.friend.FriendConstructor;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Friend extends Command {
    public Friend() {
        super("Friend", "Adds, removes, or list's all Friends.", new String[]{"f", "add/remove/list"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager", "Please specify if you would like to add, remove, or list Friend.", true);
            MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify if you would like to add, remove, or list Friend."));
            return;
        }
        switch (args[1]) {
            case "add":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager","Please specify which player you would like to add to the Friend list.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify which player you would like to add to the Friend list."));
                    return;
                }
                String playerName = args[2];
                if (Yeehaw.INSTANCE.friendManager.isFriend(playerName)) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager",playerName + " is already a friend.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(playerName + " is already a friend."));
                } else {
                    Yeehaw.INSTANCE.friendManager.addFriend(playerName);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager","Added " + playerName + " to the Friend list.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(playerName + " to the Friend list."));
                }
                break;
            case "remove":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager","Please specify which player you would like to remove from the Friend list.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Please specify which player you would like to remove from the Friend list."));
                    return;
                }
                String playerName2 = args[2];
                if (Yeehaw.INSTANCE.friendManager.isFriend(playerName2)) {
                    Yeehaw.INSTANCE.friendManager.delFriend(playerName2);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager","Removed " + playerName2 + " from Friend list.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("Removed " + playerName2 + " from Friend list."));
                } else {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager",playerName2 + " is already not a friend.", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(playerName2 + " is already not a friend."));
                }
                break;
            case "list":
                File friendFile = new File(ConfigManager.clientPath, "Friends.txt");
                try {
                    List<String> friends = Files.readAllLines(Paths.get(friendFile.toURI()));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as " + ChatFormatting.WHITE + "Friend List"));
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as " + ChatFormatting.WHITE + "Friend List"));
                    for (String friend : friends) {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(friend));
                        MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(friend));
                    }
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager","----------", true);
                    MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent("----------"));
                    for(FriendConstructor friendConstructor : Yeehaw.INSTANCE.friendManager.getFriends()) {
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend Manager", friendConstructor.getName(), true);
                        MinecraftForge.EVENT_BUS.post(new ConsoleMessageEvent(friendConstructor.getName()));
                    }
                } catch (IOException ignored) {
                }
                break;
        }
    }
}
