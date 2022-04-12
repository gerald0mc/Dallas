package me.gerald.dallas.mod.mods;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.NumberSetting;

public class GUI extends Module {
    public GUI() {
        super("GUI", Category.CLIENT, "Displays the client GUI.");
    }

    public NumberSetting red = register(new NumberSetting("Red", 255, 0, 255));
    public NumberSetting green = register(new NumberSetting("Green", 0, 0, 255));
    public NumberSetting blue = register(new NumberSetting("Blue", 0, 0, 255));

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        toggle();
    }
}
