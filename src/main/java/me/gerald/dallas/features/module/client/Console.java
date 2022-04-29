package me.gerald.dallas.features.module.client;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ConsoleMessageEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Console extends Module {
    public Console() {
        super("Console", Category.CLIENT, "Allows you to perform client commands in a GUI.");
        setKeybind(Keyboard.KEY_GRAVE);
    }

    public BooleanSetting clientMessages = new BooleanSetting("ClientMessages", true);

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Yeehaw.INSTANCE.consoleGUI);
        toggle();
    }
}
