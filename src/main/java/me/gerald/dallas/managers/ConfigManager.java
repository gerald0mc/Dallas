package me.gerald.dallas.managers;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.*;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigManager {
    public static File mainPath;
    public static File clientPath;
    public static File configPath;
    public static File currentConfigPath;
    public static File rarityPath;

    public static String currentConfig = "";

    public ConfigManager() {
        mainPath = new File(Minecraft.getMinecraft().gameDir, "Dallas");
        clientPath = new File(mainPath, "Client");
        configPath = new File(mainPath, "Configs");
        currentConfigPath = new File(configPath, "Current");
        rarityPath = new File(clientPath, "Rarities");
        if (!mainPath.exists()) {
            configPath.mkdirs();
            clientPath.mkdir();
            currentConfigPath.mkdir();
            rarityPath.mkdir();
        }
        if (!clientPath.exists())
            clientPath.mkdir();
        if (!configPath.exists())
            configPath.mkdir();
        if(!currentConfigPath.exists())
            currentConfigPath.mkdir();
        if(!rarityPath.exists())
            rarityPath.mkdir();
    }

    public static void save(String configName) throws IOException {
        String[] strings = configName.split(" ");
        if(strings.length > 1) {
            System.out.println("Config can only be 1 word long for loading purposes.");
            return;
        }
        File config;
        if(!configName.equalsIgnoreCase("current")) {
            config = new File(configPath, configName);
            if(!config.exists()) {
                config.mkdir();
            }
        } else {
            config = new File(configPath, "Current");
        }
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            File moduleFile = new File(config, module.getName() + ".txt");
            if (moduleFile.exists())
                moduleFile.delete();
            moduleFile.createNewFile();
            FileWriter fileWriter = new FileWriter(moduleFile, true);
            fileWriter.write("Name " + module.getName() + "\n");
            fileWriter.write("Bind " + Keyboard.getKeyName(module.getKeybind()) + "\n");
            fileWriter.write("Enabled " + module.isEnabled() + "\n");
            for (Setting setting : module.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((BooleanSetting) setting).getValue() + "\n");
                } else if (setting instanceof NumberSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((NumberSetting) setting).getValue() + "\n");
                } else if (setting instanceof ModeSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((ModeSetting) setting).getMode() + "\n");
                } else if (setting instanceof StringSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((StringSetting) setting).getValue() + "\n");
                } else if (setting instanceof ColorSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((ColorSetting) setting).getR() + " " + ((ColorSetting) setting).getG() + " " + ((ColorSetting) setting).getB() + " " + ((ColorSetting) setting).getA() + "\n");
                }
            }
            if(module.getCategory().equals(Module.Category.HUD)) {
                HUDModule hudModule = (HUDModule) module;
                fileWriter.write("X " + hudModule.getContainer().x + "\n");
                fileWriter.write("Y " + hudModule.getContainer().y + "\n");
            }
            fileWriter.close();
        }
    }

    public static void saveModule(Module mod, String configName) throws IOException {
        String[] strings = configName.split(" ");
        if(strings.length > 1) {
            System.out.println("Config can only be 1 word long for loading purposes.");
            return;
        }
        File config;
        if(configName.equalsIgnoreCase("current")) {
            config = new File(configPath, "Current");
        } else {
            config = new File(configPath, configName);
            if(!config.exists()) {
                System.out.println("This config doesn't exist.");
                return;
            }
        }
        File moduleFile = new File(config, mod.getName() + ".txt");
        if (moduleFile.exists())
            moduleFile.delete();
        moduleFile.createNewFile();
        FileWriter fileWriter = new FileWriter(moduleFile, true);
        fileWriter.write("Name " + mod.getName() + "\n");
        fileWriter.write("Bind " + Keyboard.getKeyName(mod.getKeybind()) + "\n");
        fileWriter.write("Enabled " + mod.isEnabled() + "\n");
        for (Setting setting : mod.getSettings()) {
            if (setting instanceof BooleanSetting) {
                fileWriter.write("Setting " + setting.getName() + " " + ((BooleanSetting) setting).getValue() + "\n");
            } else if (setting instanceof NumberSetting) {
                fileWriter.write("Setting " + setting.getName() + " " + ((NumberSetting) setting).getValue() + "\n");
            } else if (setting instanceof ModeSetting) {
                fileWriter.write("Setting " + setting.getName() + " " + ((ModeSetting) setting).getMode() + "\n");
            } else if (setting instanceof StringSetting) {
                fileWriter.write("Setting " + setting.getName() + " " + ((StringSetting) setting).getValue() + "\n");
            } else if (setting instanceof ColorSetting) {
                fileWriter.write("Setting " + setting.getName() + " " + ((ColorSetting) setting).getR() + " " + ((ColorSetting) setting).getG() + " " + ((ColorSetting) setting).getB() + " " + ((ColorSetting) setting).getA() + "\n");
            }
        }
        if(mod.getCategory().equals(Module.Category.HUD)) {
            HUDModule hudModule = (HUDModule) mod;
            fileWriter.write("X " + hudModule.getContainer().x + "\n");
            fileWriter.write("Y " + hudModule.getContainer().y + "\n");
        }
        fileWriter.close();
        return;
    }

    public static void load(String configName) {
        String[] strings = configName.split(" ");
        if(strings.length > 1) {
            System.out.println("Config can only be 1 word long for loading purposes.");
            return;
        }
        File config;
        if(configName.equalsIgnoreCase("current")) {
            config = new File(configPath, "Current");
        } else {
            config = new File(configPath, configName);
            if(!config.exists()) {
                System.out.println("This config doesn't exist.");
                return;
            }
        }
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            File moduleFile = new File(config, module.getName() + ".txt");
            if (moduleFile.exists()) {
                try {
                    List<String> lines = Files.readAllLines(Paths.get(moduleFile.toURI()), StandardCharsets.UTF_8);
                    for (String line : lines) {
                        String[] words = line.split(" ");
                        switch (words[0]) {
                            case "Bind":
                                try {
                                    String bind = words[1];
                                    module.setKeybind(Keyboard.getKeyIndex(bind));
                                    System.out.println("Set modules " + module.getName() + " bind to " + module.getKeybind());
                                } catch (Exception e) {
                                    System.out.println("Couldn't set " + module.getName() + "'s bind for some reason.");
                                }
                                break;
                            case "Enabled":
                                try {
                                    String status = words[1];
                                    if (status.equalsIgnoreCase("true")) {
                                        if (!module.isEnabled())
                                            module.toggle();
                                        System.out.println("Set modules " + module.getName() + " to Toggled");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Couldn't enable " + module.getName() + " for some reason.");
                                }
                                break;
                            case "Setting":
                                String settingName = words[1];
                                for (Setting setting : module.getSettings()) {
                                    if (setting.getName().equalsIgnoreCase(settingName)) {
                                        try {
                                            String settingValue = words[2];
                                            if (setting instanceof BooleanSetting) {
                                                ((BooleanSetting) setting).setValue(Boolean.parseBoolean(settingValue));
                                                System.out.println("Set setting " + setting.getName() + " to " + ((BooleanSetting) setting).getValue());
                                            } else if (setting instanceof NumberSetting) {
                                                ((NumberSetting) setting).setValue(Float.parseFloat(settingValue));
                                                System.out.println("Set setting " + setting.getName() + " to " + ((NumberSetting) setting).getValue());
                                            } else if (setting instanceof ModeSetting) {
                                                ((ModeSetting) setting).setMode(settingValue);
                                                System.out.println("Set setting " + setting.getName() + " to " + ((ModeSetting) setting).getMode());
//                                            } else if (setting instanceof StringSetting) {
//                                                String[] value = getStringValue(setting, words);
//                                                ((StringSetting) setting).setValue(Arrays.toString(value));
//                                                System.out.println("Set setting " + setting.getName() + " to " + ((StringSetting) setting).getValue());
                                            } else if (setting instanceof ColorSetting) {
                                                int red = Integer.parseInt(words[2]);
                                                int green = Integer.parseInt(words[3]);
                                                int blue = Integer.parseInt(words[4]);
                                                int alpha = Integer.parseInt(words[5]);
                                                ((ColorSetting) setting).setR(red);
                                                ((ColorSetting) setting).setG(green);
                                                ((ColorSetting) setting).setB(blue);
                                                ((ColorSetting) setting).setA(alpha);
                                                System.out.println("Set setting " + setting.getName() + " to " + ((ColorSetting) setting).getR() + ", " + ((ColorSetting) setting).getG() + ", " + ((ColorSetting) setting).getB() + ", " + ((ColorSetting) setting).getA());
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Couldn't load setting " + setting.getName() + " for some reason.");
                                        }
                                    }
                                }
                                break;
                            case "X":
                                HUDModule hudModule = (HUDModule) module;
                                hudModule.getContainer().x = Integer.parseInt(words[1]);
                                break;
                            case "Y":
                                HUDModule hudModule2 = (HUDModule) module;
                                hudModule2.getContainer().y = Integer.parseInt(words[1]);
                                break;
                        }
                    }
                } catch (IOException io) {
                    System.out.println("Couldn't load modules " + module.getName() + " for some reason.");
                }
            } else {
                System.out.println("Couldn't find modules " + module.getName() + " for some reason.");
            }
        }
        currentConfig = configName;
    }
}
