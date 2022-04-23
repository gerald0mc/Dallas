package me.gerald.dallas.features.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import org.lwjgl.opengl.Display;

public class Window extends Module {
    StringSetting windowName = register(new StringSetting("WindowName", "Dallas 1.0"));

    public Window() {
        super("Window", Category.MISC, "Customize the Minecraft window.");
    }

    @Override
    public void onEnable() {
        Display.setTitle(windowName.getValue());
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Window", "Set Minecraft window to " + ChatFormatting.AQUA + windowName.getValue(), true);
        toggle();
    }
}
