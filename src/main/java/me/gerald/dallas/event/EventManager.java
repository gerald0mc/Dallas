package me.gerald.dallas.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.utils.ConfigManager;
import me.gerald.dallas.utils.MessageUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class EventManager {
    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for(Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if(module.getKeybind() == key) {
                    module.toggle();
                        MessageUtils.sendMessage(ChatFormatting.BOLD + module.getName() + ChatFormatting.BOLD + (module.isEnabled() ? ChatFormatting.BOLD + (ChatFormatting.GREEN + " ENABLED") : ChatFormatting.BOLD + (ChatFormatting.RED + " DISABLED")));
                }
            }
        }
    }

    @SubscribeEvent
    public void onModuleToggle(ModuleToggleEvent event) throws IOException {
        ConfigManager.save();
    }
}
