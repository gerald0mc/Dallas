package me.gerald.dallas.event;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.ModuleToggleEvent;
import me.gerald.dallas.event.listeners.TotemPopListener;
import me.gerald.dallas.features.command.Command;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.utils.Globals;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class EventManager implements Globals {
    private final List<Module> hudModules;
    public TotemPopListener totemPopListener;

    public EventManager() {
        MinecraftForge.EVENT_BUS.register(this);
        totemPopListener = new TotemPopListener();
        hudModules = Yeehaw.INSTANCE.moduleManager.getCategory(Module.Category.HUD);
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

    //config save
    @SubscribeEvent
    public void onModuleToggle(ModuleToggleEvent event) throws IOException {
        ConfigManager.save();
    }

    //module enable
    @SubscribeEvent
    public void onModuleEnable(ModuleToggleEvent.Enable event) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Module Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.GREEN + "enabled" + ChatFormatting.RESET + "!", true);
    }

    //module disable
    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Disable event) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Module Toggle", ChatFormatting.AQUA + event.getModule().getName() + ChatFormatting.RESET + " has been " + ChatFormatting.RED + "disabled" + ChatFormatting.RESET + "!", true);
    }

//    @SubscribeEvent
//    public void onRender(RenderGameOverlayEvent.Text event) {
//        if(Module.nullCheck()) return;
//        if(notificationHistory.isEmpty()) return;
//        if(mc.currentScreen instanceof GuiChat) {
//            Color color;
//            if(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
//                color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
//            }else {
//                color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
//            }
//            if(Yeehaw.INSTANCE.notificationManager.notifications.isEmpty() || !Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).isEnabled()) return;
//            int height = Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).title.getValue() ? 26 : 13;
//            int yOffset = 0;
//            for(NotificationConstructor notificationConstructor : NotificationManager.notificationHistory) {
//                if(!Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).title.getValue()) {
//                    Gui.drawRect(0, 2 + yOffset, mc.fontRenderer.getStringWidth(notificationConstructor.getMessage() + 6), 2 + height + yOffset, new Color(0, 0, 0, 170).getRGB());
//                    Gui.drawRect(0, 2 + yOffset, 2, 2 + height + yOffset, color.getRGB());
//                    mc.fontRenderer.drawStringWithShadow(notificationConstructor.getMessage(), 4, 4 + yOffset, -1);
//                }else {
//                    Gui.drawRect(0, 2 + yOffset, mc.fontRenderer.getStringWidth(notificationConstructor.getMessage() + 6), 2 + height + yOffset, new Color(0, 0, 0, 170).getRGB());
//                    Gui.drawRect(0, 2 + yOffset, 2, 2 + height + yOffset, color.getRGB());
//                    mc.fontRenderer.drawStringWithShadow(notificationConstructor.getTitle(), 4, 4 + yOffset, -1);
//                    mc.fontRenderer.drawStringWithShadow(notificationConstructor.getMessage(), 4, 17 + yOffset, -1);
//                }
//                yOffset += height + 2;
//            }
//        }
//    }
}
