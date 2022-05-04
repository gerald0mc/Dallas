package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.utils.MessageUtil;

public class Help extends Command {
    public Help() {
        super("Help", "Help menu for the client.", new String[]{"help"});
    }

    @Override
    public void onCommand(String[] args) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Help", ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.RESET + " Help Menu", true);
        for (Command command : Yeehaw.INSTANCE.commandManager.getCommands()) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Help", ChatFormatting.AQUA + String.join(" ", command.getUsage()) + ChatFormatting.GRAY + ": " + ChatFormatting.GREEN + command.getDescription(), true);
        }
    }
}
