package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Friend extends Command {
    public Friend() {
        super("Friend", "Adds, removes, or list's all Friends.", new String[]{"f", "[<add> <name>/<remove> <name>/<list>]"});
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", "Please specify if you would like to add, remove, or list Friend.", true);
            return;
        }
        switch (args[1]) {
            case "add":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", "Please specify which player you would like to add to the Friend list.", true);
                    return;
                }
                String playerName = args[2];
                if (Yeehaw.INSTANCE.friendManager.isFriend(playerName)) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", playerName + " is already a friend.", true);
                } else {
                    Yeehaw.INSTANCE.friendManager.addFriend(playerName);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", "Added " + playerName + " to the Friend list.", true);
                }
                break;
            case "remove":
                if (args.length == 2) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", "Please specify which player you would like to remove from the Friend list.", true);
                    return;
                }
                String playerName2 = args[2];
                if (Yeehaw.INSTANCE.friendManager.isFriend(playerName2)) {
                    Yeehaw.INSTANCE.friendManager.delFriend(playerName2);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", "Removed " + playerName2 + " from Friend list.", true);
                } else {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "Friend", playerName2 + " is already not a friend.", true);
                }
                break;
            case "list":
                File friendFile = new File(ConfigManager.clientPath, "Friends.txt");
                try {
                    List<String> friends = Files.readAllLines(Paths.get(friendFile.toURI()));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as " + ChatFormatting.WHITE + "Friend List"));
                    for (String friend : friends) {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(friend));
                    }
                } catch (IOException ignored) { }
                break;
        }
    }
}
