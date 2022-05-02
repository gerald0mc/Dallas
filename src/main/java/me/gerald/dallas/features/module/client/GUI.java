package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class GUI extends Module {
    public ColorSetting color = new ColorSetting("Color", 127, 0, 255, 255);
    public BooleanSetting rainbow = new BooleanSetting("Rainbow", false);
    public NumberSetting rainbowSpeed = new NumberSetting("RainbowSpeed", 3, 1, 10, () -> rainbow.getValue());
    public ModeSetting categoryAlignment = new ModeSetting("CategoryAlignment", "Middle", "Left", "Middle", "Right");
    public ModeSetting moduleAlignment = new ModeSetting("ModuleAlignment", "Middle", "Left", "Middle", "Right");
    public BooleanSetting moduleCount = new BooleanSetting("ModuleCount", true);

    public GUI() {
        super("GUI", Category.CLIENT, "Displays the client GUI.");
    }

    @Override
    public void onEnable() {
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = false;
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = true;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = false;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = true;
        mc.displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        toggle();
    }
}
