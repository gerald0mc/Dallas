package me.gerald.dallas.mod.mods.misc;

import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import org.lwjgl.opengl.Display;

public class Window extends Module {
    public Window() {
        super("Window", Category.MISC, "Customize the Minecraft window.");
    }
    
    StringSetting windowName = new StringSetting("WindowName", "Dallas 1.0");
    
    @Override
    public void onEnable() {
        Display.setTitle(windowName.getValue());
        MessageUtil.sendMessage("Set Minecraft window to " + windowName.getValue());
        toggle();
    }
}
