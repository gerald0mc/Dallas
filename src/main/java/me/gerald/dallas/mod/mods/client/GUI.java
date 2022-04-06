package me.gerald.dallas.mod.mods.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ConfigManager;

import java.io.IOException;

public class GUI extends Module {
    public GUI() {
        super("GUI", Category.CLIENT, "Displays the client GUI.");
    }

    public NumberSetting red = register(new NumberSetting("Red", 255, 0, 255));
    public NumberSetting green = register(new NumberSetting("Green", 0, 0, 255));
    public NumberSetting blue = register(new NumberSetting("Blue", 0, 0, 255));
    public ModeSetting categoryAllignment = register(new ModeSetting("CategoryAlignment", "Middle", "Left", "Middle", "Right"));
    public ModeSetting moduleAlignment = register(new ModeSetting("ModuleAlignment", "Middle", "Left", "Middle", "Right"));

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        toggle();
    }
}
