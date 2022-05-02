package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import org.lwjgl.input.Keyboard;

public class Console extends Module {
    public BooleanSetting clientMessages = new BooleanSetting("ClientMessages", true);

    public Console() {
        super("Console", Category.CLIENT, "Allows you to perform client commands in a GUI.");
        setKeybind(Keyboard.KEY_GRAVE);
    }

    @Override
    public void onEnable() {
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = true;
        Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = false;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = true;
        Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = false;
        mc.displayGuiScreen(Yeehaw.INSTANCE.consoleGUI);
        toggle();
    }
}
