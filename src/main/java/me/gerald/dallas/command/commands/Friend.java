package me.gerald.dallas.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.command.Command;
import me.gerald.dallas.utils.ConfigManager;
import me.gerald.dallas.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Friend extends Command {
    public Friend() {
        super("Friend", "Adds, removes, or list's all friends.", new String[] {"f", "add/remove/list"});
    }

    @Override
    public void onCommand(String[] args) {
        if(args.length == 1) {
            MessageUtils.sendMessage("Please specify if you would like to add, remove, or list friends.");
            return;
        }
        switch (args[1]) {
            case "add":
                if(args.length == 2) {
                    MessageUtils.sendMessage("Please specify which player you would like to add to the friends list.");
                    return;
                }
                String playerName = args[2];
                if(Yeehaw.INSTANCE.friendManager.isFriend(playerName)) {
                    MessageUtils.sendMessage(playerName + " is already a friend.");
                }else {
                    Yeehaw.INSTANCE.friendManager.addFriend(playerName);
                    MessageUtils.sendMessage("Added " + playerName + " to the friends list.");
                }
                break;
            case "remove":
                if(args.length == 2) {
                    MessageUtils.sendMessage("Please specify which player you would like to remove from the friends list.");
                    return;
                }
                String playerName2 = args[2];
                if(Yeehaw.INSTANCE.friendManager.isFriend(playerName2)) {
                    Yeehaw.INSTANCE.friendManager.delFriend(playerName2);
                    MessageUtils.sendMessage("Removed " + playerName2 + " from friends list.");
                }else {
                    MessageUtils.sendMessage(playerName2 + " is already not a friend.");
                }
                break;
            case "list":
                File friendFile = new File(ConfigManager.clientPath, "Friends.txt");
                try {
                    List<String> friends = Files.readAllLines(Paths.get(friendFile.toURI()));
                    Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as " + ChatFormatting.WHITE + "Friend List"));
                    for(String friend : friends) {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(friend));
                    }
                }catch (IOException ignored) {}
                break;
        }
    }
}
