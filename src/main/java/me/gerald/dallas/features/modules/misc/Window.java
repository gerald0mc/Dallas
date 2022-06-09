package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import org.lwjgl.opengl.Display;

public class Window extends Module {
    public StringSetting windowName = new StringSetting("WindowName", "Dallas 1.0", "New window name.");

    public Window() {
        super("Window", Category.MISC, "Customize the Minecraft window.");
    }

    @Override
    public void onEnable() {
        Display.setTitle(windowName.getValue());
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Window", "Set Minecraft window title to " + ChatFormatting.AQUA + windowName.getValue(), true);
        toggle();
    }
}
