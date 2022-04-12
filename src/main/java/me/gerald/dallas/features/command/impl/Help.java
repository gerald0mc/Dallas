package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class Help extends Command {
    public Help() {
        super("Help", "Help menu for the client.", new String[] {"help"});
    }

    @Override
    public void onCommand(String[] args) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.RESET + " Help Menu"));
        for(Command command : Yeehaw.INSTANCE.commandManager.getCommands()) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(ChatFormatting.AQUA + String.join(" ", command.getUsage()) + ChatFormatting.GRAY + ": " + ChatFormatting.GREEN + command.getDescription()));
        }
    }
}
