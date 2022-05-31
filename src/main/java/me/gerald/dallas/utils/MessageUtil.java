package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.console.ConsoleGUI;
import me.gerald.dallas.features.modules.client.Console;
import me.gerald.dallas.features.modules.hud.notification.Notifications;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
    public static String clientPrefix = ChatFormatting.DARK_GRAY + "<" + ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.DARK_GRAY + "> " + ChatFormatting.RESET;

    public static void sendMessage(String title, String message, boolean clientMessage) {
        if (Module.nullCheck()) return;
        Yeehaw.INSTANCE.eventManager.clientHistory.add(ChatFormatting.GRAY + "[" + ChatFormatting.RESET + title + ChatFormatting.GRAY + "]: " + ChatFormatting.RESET + message);
        if (Yeehaw.INSTANCE.moduleManager.getModule(Console.class).clientMessages.getValue()) {
            Yeehaw.INSTANCE.consoleGUI.messageHistory.add(message);
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) return;
        }
        if (Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).isEnabled()) {
            if (clientMessage && !Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).clientMessages.getValue())
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
            else
                Yeehaw.INSTANCE.notificationManager.addNotification(title, message, System.currentTimeMillis());
        } else {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
        }
    }

    public static void sendRemovableMessage(String title, String message, boolean clientMessage, int id) {
        if (Module.nullCheck()) return;
        Yeehaw.INSTANCE.eventManager.clientHistory.add(ChatFormatting.GRAY + "[" + ChatFormatting.RESET + title + ChatFormatting.GRAY + "]: " + ChatFormatting.RESET + message);
        if (Yeehaw.INSTANCE.moduleManager.getModule(Console.class).clientMessages.getValue()) {
            Yeehaw.INSTANCE.consoleGUI.messageHistory.add(message);
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) return;
        }
        if (Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).isEnabled()) {
            if (clientMessage && !Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).clientMessages.getValue())
                Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(clientPrefix + message), id);
            else
                Yeehaw.INSTANCE.notificationManager.addNotification(title, message, System.currentTimeMillis());
        } else {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(clientPrefix + message), id);
        }
    }
}
