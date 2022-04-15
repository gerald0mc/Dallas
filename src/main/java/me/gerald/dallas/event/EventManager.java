package me.gerald.dallas.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class EventManager {
    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    //binds
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if (module.getKeybind() == key) {
                    module.toggle();
                }
            }
        }
    }

    //commands
    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(Yeehaw.INSTANCE.commandManager.PREFIX)) {
            event.setCanceled(true);
            for (Command command : Yeehaw.INSTANCE.commandManager.getCommands()) {
                if (args[0].equalsIgnoreCase((Yeehaw.INSTANCE.commandManager.PREFIX + command.getName()))) {
                    command.onCommand(args);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI) return;
        for (Module module : Yeehaw.INSTANCE.moduleManager.getCategory(Module.Category.HUD)) {
            if (module.isEnabled()) {
                final HUDModule hudMod = (HUDModule) module;
                hudMod.getContainer().drawScreen(-1, -1, event.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onModuleToggle(ModuleToggleEvent event) throws IOException {
        ConfigManager.save();
    }

    @SubscribeEvent
    public void onModuleEnable(ModuleToggleEvent.Enable event) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.GREEN + " enabled");
    }

    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Disable event) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + event.getModule().getName() + ChatFormatting.RED + " disabled");
    }
}
