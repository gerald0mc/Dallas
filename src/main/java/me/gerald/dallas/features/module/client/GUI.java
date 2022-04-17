package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class GUI extends Module {
    public ColorSetting color = register(new ColorSetting("Color", 127, 0, 255, 255));
    public BooleanSetting rainbow = register(new BooleanSetting("Rainbow", false));
    public NumberSetting rainbowSpeed = register(new NumberSetting("RainbowSpeed", 3, 1, 10, () -> rainbow.getValue()));
    public ModeSetting categoryAlignment = register(new ModeSetting("CategoryAlignment", "Middle", "Left", "Middle", "Right"));
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
