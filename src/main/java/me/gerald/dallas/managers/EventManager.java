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
import me.gerald.dallas.utils.WebhookUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventManager implements Globals {
    private final List<Module> hudModules;
    public TotemPopListener totemPopListener;
    public DamageListener damageListener;

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
        totemPopListener = new TotemPopListener();
        damageListener = new DamageListener();
        hudModules = Yeehaw.INSTANCE.moduleManager.getCategory(Module.Category.HUD);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (Module.nullCheck()) return;
        if (!Yeehaw.INSTANCE.clickGUI.searchBox.entryString.equals("Search")) {
            if (!(mc.currentScreen instanceof ClickGUI)) {
                Yeehaw.INSTANCE.clickGUI.searchBox.searchModules.clear();
                Yeehaw.INSTANCE.clickGUI.searchBox.listening = false;
            }
        }
    }

    @SubscribeEvent
    public void onModuleToggle(ModuleToggleEvent event) throws IOException {
        ConfigManager.saveModule(event.getModule(), "Current");
    }

    //binds
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if (module.getKeybind() == key && key != Keyboard.KEY_NONE) {
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
                MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", 50, MessageUtil.MessageType.GENERAL);
                break;
            case "Simple":
                MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", 50, MessageUtil.MessageType.GENERAL);
                break;
        }
    }

    //modules disable
    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Disable event) {
        if (!Yeehaw.INSTANCE.moduleManager.getModule(Client.class).toggleMessage.getValue()) return;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(Client.class).messageMode.getMode()) {
            case "Default":
                MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!",50, MessageUtil.MessageType.GENERAL);
                break;
            case "Simple":
                MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!", 50, MessageUtil.MessageType.GENERAL);
                break;
        }
    }
}
