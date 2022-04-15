package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class GUI extends Module {
    public NumberSetting red = register(new NumberSetting("Red", 127, 0, 255));
    public NumberSetting green = register(new NumberSetting("Green", 0, 0, 255));
    public NumberSetting blue = register(new NumberSetting("Blue", 255, 0, 255));
    public ModeSetting categoryAllignment = register(new ModeSetting("CategoryAlignment", "Middle", "Left", "Middle", "Right"));
    public ModeSetting moduleAlignment = register(new ModeSetting("ModuleAlignment", "Middle", "Left", "Middle", "Right"));
    public GUI() {
        super("GUI", Category.CLIENT, "Displays the client GUI.");
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        toggle();
    }
}
