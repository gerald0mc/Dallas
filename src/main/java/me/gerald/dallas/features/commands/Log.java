package me.gerald.dallas.features.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.*;
import me.gerald.dallas.utils.MessageUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Log extends Command {
    public Log() {
        super("Log", "Logs things.", new String[]{"log"});
    }

    @Override
    public void onCommand(String[] args) {
        super.onCommand(args);
        if (args.length > 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Log", "This command is just log", MessageUtil.MessageType.CONSTANT);
            return;
        }
        File moduleLogFile = new File(ConfigManager.clientPath, "ModuleLog.txt");
        if (moduleLogFile.exists())
            moduleLogFile.delete();
        try {
            moduleLogFile.createNewFile();
            FileWriter writer = new FileWriter(moduleLogFile);
            writer.write("Version: " + Yeehaw.VERSION + "\n");
            writer.write("Is Dev?: " + Yeehaw.INSTANCE.isDevJar + "\n");
            writer.write("Module Count: " + Yeehaw.INSTANCE.moduleManager.getModules().size() + "\n");
            for (Module.Category category : Module.Category.values()) {
                writer.write(category.name() + " [" + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + "]" + "\n");
                for (Module module : Yeehaw.INSTANCE.moduleManager.getCategory(category)) {
                    writer.write("* " + module.getName() + " " + module.getDescription() + "\n");
                    if (module.getSettings() != null) {
                        for (Setting setting : module.getSettings()) {
                            if (setting instanceof BooleanSetting)
                                writer.write("  - " + setting.getName() + " Value: " + ((BooleanSetting) setting).getValue() + " -> " + setting.getDescription() + "\n");
                            else if (setting instanceof NumberSetting)
                                writer.write("  - " + setting.getName() + " Value: " + ((NumberSetting) setting).getValue() + " Min: " + ((NumberSetting) setting).getMin() + " Max: " + ((NumberSetting) setting).getMax() + " -> " + setting.getDescription() + "\n");
                            else if (setting instanceof ModeSetting)
                                writer.write("  - " + setting.getName() + " Mode: " + ((ModeSetting) setting).getMode() + " Default: " + ((ModeSetting) setting).getDefaultMode() + " Modes: " + Arrays.toString(((ModeSetting) setting).getModes()) + " -> " + setting.getDescription() + "\n");
                            else if (setting instanceof StringSetting)
                                writer.write("  - " + setting.getName() + " Value: " + ((StringSetting) setting).getValue() + " -> " + setting.getDescription() + "\n");
                            else if (setting instanceof ColorSetting)
                                writer.write("  - " + setting.getName() + " R: " + ((ColorSetting) setting).getR() + " G: " + ((ColorSetting) setting).getG() + " B: " + ((ColorSetting) setting).getB() + " A: " + ((ColorSetting) setting).getA() + " -> " + setting.getDescription() + "\n");
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException ignored) {
        }
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Log", "Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "ModuleLog.txt" + ChatFormatting.RESET + " to view all modules and settings.", MessageUtil.MessageType.CONSTANT);
    }
}
