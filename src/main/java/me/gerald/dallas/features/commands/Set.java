package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtil;

public class Set extends Command {
    public Set() {
        super("Set", "Set various stuff.", new String[]{"set", "<setting>", "<value>"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Please enter what setting and value you wish to set.", MessageUtil.MessageType.CONSTANT);
            return;
        }
        String settingName = args[1];
        if (args.length == 2) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Please enter the value you wish to set.", MessageUtil.MessageType.CONSTANT);
            return;
        }
        String value = args[2];
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            for (Setting setting : module.getSettings()) {
                if (setting.getName().equalsIgnoreCase(settingName)) {
                    if (setting instanceof BooleanSetting) {
                        ((BooleanSetting) setting).setValue(Boolean.parseBoolean(value));
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Set setting " + ChatFormatting.AQUA + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + ((BooleanSetting) setting).getValue(), MessageUtil.MessageType.CONSTANT);
                    } else if (setting instanceof NumberSetting) {
                        if (Integer.parseInt(value) > ((NumberSetting) setting).getMax()) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "The value you have entered is larger then this settings max value.", MessageUtil.MessageType.CONSTANT);
                            return;
                        } else if (Integer.parseInt(value) < ((NumberSetting) setting).getMin()) {
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "The value you have entered is smaller then this settings min value.", MessageUtil.MessageType.CONSTANT);
                            return;
                        }
                        ((NumberSetting) setting).setValue(Float.parseFloat(value));
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Set setting " + ChatFormatting.AQUA + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + ((NumberSetting) setting).getValue(), MessageUtil.MessageType.CONSTANT);
                    } else if (setting instanceof ModeSetting) {
                        ((ModeSetting) setting).setMode(value);
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "Set", "Set setting " + ChatFormatting.AQUA + setting.getName() + ChatFormatting.RESET + " to " + ChatFormatting.GREEN + ((ModeSetting) setting).getMode(), MessageUtil.MessageType.CONSTANT);
                    }
                    return;
                }
            }
        }
    }
}
