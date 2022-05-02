package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.MessageUtil;

public class Toggle extends Command {
    public Toggle() {
        super("Toggle", "Toggles a module.", new String[]{"toggle", "[module]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", "Please specify which module you would like to toggle.", true);
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
