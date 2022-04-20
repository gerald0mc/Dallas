package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import org.lwjgl.input.Keyboard;

public class Console extends Module {
    public Console() {
        super("Console", Category.CLIENT, "Allows you to perform client commands in a GUI.");
        setKeybind(Keyboard.KEY_GRAVE);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Yeehaw.INSTANCE.consoleGUI);
        toggle();
    }
}
