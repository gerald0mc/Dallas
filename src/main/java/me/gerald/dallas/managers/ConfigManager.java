package me.gerald.dallas.managers;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager {
    public static File mainPath;
    public static File clientPath;
    public static File modulePath;

    public ConfigManager() {
        mainPath = new File(Minecraft.getMinecraft().gameDir, "Dallas");
        clientPath = new File(mainPath, "Client");
        modulePath = new File(mainPath, "Modules");
        if(!mainPath.exists()) {
            modulePath.mkdirs();
            clientPath.mkdir();
        }
        if(!modulePath.exists()) {
            modulePath.mkdir();
        }
        if(!clientPath.exists()) {
            clientPath.mkdir();
        }
    }

    public static void save() throws IOException {
        for(Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            File moduleFile = new File(modulePath, module.getName() + ".txt");
            if(moduleFile.exists())
                moduleFile.delete();
            moduleFile.createNewFile();
            FileWriter fileWriter = new FileWriter(moduleFile, true);
            fileWriter.write("Name " + module.getName() + "\n");
            fileWriter.write("Bind " + Keyboard.getKeyName(module.getKeybind()) + "\n");
            fileWriter.write("Enabled " + module.isEnabled() + "\n");
            for(Setting setting : module.getSettings()) {
                if(setting instanceof BooleanSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((BooleanSetting) setting).getValue() + "\n");
                }else if(setting instanceof NumberSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((NumberSetting) setting).getValue() + "\n");
                }else if(setting instanceof ModeSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((ModeSetting) setting).getMode() + "\n");
                }else if(setting instanceof StringSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((StringSetting) setting).getValue() + "\n");
                }else if(setting instanceof ColorSetting) {
                    fileWriter.write("Setting " + setting.getName() + " " + ((ColorSetting) setting).getR() + " " + ((ColorSetting) setting).getG() + " " + ((ColorSetting) setting).getB() + ((ColorSetting) setting).getA() + "\n");
                }
            }
            fileWriter.close();
        }
    }

    public static void load() {
        for(Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            File moduleFile = new File(modulePath, module.getName() + ".txt");
            if(moduleFile.exists()) {
                try {
                    List<String> lines = Files.readAllLines(Paths.get(moduleFile.toURI()), StandardCharsets.UTF_8);
                    System.out.println(lines);
                    for (String line : lines) {
                        String[] words = line.split("\\W+");
                        switch (words[0]) {
                            case "Bind":
                                String bind = words[1];
                                module.setKeybind(Keyboard.getKeyIndex(bind));
                                break;
                            case "Enabled":
                                String status = words[1];
                                if(status.equalsIgnoreCase("true")) {
                                    module.toggle();
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
                                            } else if (setting instanceof StringSetting) {
                                                ((StringSetting) setting).setValue(settingValue);
                                                System.out.println("Set setting " + setting.getName() + " to " + ((StringSetting) setting).getValue());
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
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                break;
                        }
                    }
                } catch (IOException io) {
                    System.out.println("Couldn't load module " + module.getName() + " for some reason.");
                }
            }else {
                System.out.println("Couldn't find module " + module.getName() + " for some reason.");
            }
        }
    }
}
