package me.gerald.dallas.features.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.common.MinecraftForge;

public class Set extends Command {
    public Set() {
        super("Set", "Set various stuff.", new String[]{"set", "[setting]", "[value]"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Please enter what setting and value you wish to set.", true);
            return;
        }
        String settingName = args[1];
        if (args.length == 2) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Please enter the value you wish to set.", true);
            return;
        }
        String value = args[2];
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            for (Setting setting : module.getSettings()) {
                if (setting.getName().equalsIgnoreCase(settingName)) {
                    if (setting instanceof BooleanSetting)
                        ((BooleanSetting) setting).setValue(Boolean.parseBoolean(value));
                    else if (setting instanceof NumberSetting)
                        ((NumberSetting) setting).setValue(Float.parseFloat(value));
                    else if (setting instanceof ModeSetting)
                        ((ModeSetting) setting).setMode(value);
                    return;
                }
            }
        }
    }
}
