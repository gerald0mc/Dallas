package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.MessageUtil;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggles a modules.", new String[]{"toggle", "<module>"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", "Please specify which modules you would like to toggle.", true);
            return;
        }
        String moduleName = args[1];
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(moduleName)) {
                module.toggle();
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", "Toggled " + ChatFormatting.AQUA + module.getName(), true);
                return;
            }
        }
    }
}
