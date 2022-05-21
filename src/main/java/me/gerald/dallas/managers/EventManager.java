package me.gerald.dallas.managers;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.event.listeners.DamageListener;
import me.gerald.dallas.event.listeners.TotemPopListener;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.modules.client.Client;
import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.managers.command.Command;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.Globals;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class EventManager implements Globals {
    public TotemPopListener totemPopListener;
    public DamageListener damageListener;

    private final List<Module> hudModules;
    public List<String> clientHistory;

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
        totemPopListener = new TotemPopListener();
        damageListener = new DamageListener();
        hudModules = Yeehaw.INSTANCE.moduleManager.getCategory(Module.Category.HUD);
        clientHistory = new ArrayList<>();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(Module.nullCheck()) return;
        if(!Yeehaw.INSTANCE.clickGUI.searchBox.entryString.equals("Search")) {
            if(!(mc.currentScreen instanceof ClickGUI)) {
                Yeehaw.INSTANCE.clickGUI.searchBox.searchModules.clear();
                Yeehaw.INSTANCE.clickGUI.searchBox.listening = false;
            }
        }
    }

    @SubscribeEvent
    public void onModuleToggle(ModuleToggleEvent event) throws IOException {
        ConfigManager.saveModule(event.getModule());
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

    //hud editor
    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.currentScreen instanceof ClickGUI || mc.currentScreen instanceof GuiChat) return;
        hudModules.forEach(module -> {
            if (module.isEnabled()) {
                ((HUDModule) module).getContainer().drawScreen(-1, -1, event.getPartialTicks());
            }
        });
    }

    //modules enable
    @SubscribeEvent
    public void onModuleEnable(ModuleToggleEvent.Enable event) {
        if (!Yeehaw.INSTANCE.moduleManager.getModule(Client.class).toggleMessage.getValue()) return;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(Client.class).messageMode.getMode()) {
            case "Default":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", true);
                break;
            case "Simple":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", true);
                break;
        }
    }

    //modules disable
    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Disable event) {
        if (!Yeehaw.INSTANCE.moduleManager.getModule(Client.class).toggleMessage.getValue()) return;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(Client.class).messageMode.getMode()) {
            case "Default":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!", true);
                break;
            case "Simple":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!", true);
                break;
        }
    }
}