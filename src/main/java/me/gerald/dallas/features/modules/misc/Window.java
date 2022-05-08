package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class Window extends Module {
    public StringSetting windowName = new StringSetting("WindowName", "Dallas 1.0");
    public BooleanSetting animation = new BooleanSetting("Animation", false);
    public NumberSetting delay = new NumberSetting("Delay", 1, 0 ,5, () -> animation.getValue());

    public Window() {
        super("Window", Category.MISC, "Customize the Minecraft window.");
    }

    public String currentTitle = windowName.getValue();
    public TimerUtil timer = new TimerUtil();

    @Override
    public void onEnable() {
        if(!animation.getValue()) {
            Display.setTitle(windowName.getValue());
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Window", "Set Minecraft window to " + ChatFormatting.AQUA + windowName.getValue(), true);
            toggle();
        }
    }

//    @SubscribeEvent
//    public void onUpdate(TickEvent.ClientTickEvent event) {
//        if(nullCheck()) return;
//        if(currentTitle.equalsIgnoreCase(windowName.getValue())) {
//            Display.setTitle("");
//            currentTitle = "";
//            return;
//        }
//        String[] chars = windowName.getValue().split("");
//        String tempString = "";
//        for(String c : chars) {
//            if(currentTitle.contains(tempString))
//            tempString += c;
//        }
//    }
}
