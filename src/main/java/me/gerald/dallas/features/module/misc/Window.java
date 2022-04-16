package me.gerald.dallas.features.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import org.lwjgl.opengl.Display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Window extends Module {
    StringSetting windowName = register(new StringSetting("WindowName", "Dallas 1.0"));

    public Window() {
        super("Window", Category.MISC, "Customize the Minecraft window.");
    }

    @Override
    public void onEnable() {
        Display.setTitle(windowName.getValue());
        MessageUtil.sendMessage("Set Minecraft window to " + ChatFormatting.AQUA + windowName.getValue());
        toggle();
    }
}
