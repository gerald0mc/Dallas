package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.console.ConsoleGUI;
import me.gerald.dallas.features.modules.client.Console;
import me.gerald.dallas.features.modules.client.Messages;
import me.gerald.dallas.features.modules.hud.notification.Notifications;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
    public static String clientPrefix = ChatFormatting.DARK_GRAY + "<" + ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.DARK_GRAY + "> " + ChatFormatting.RESET;

    public static void sendMessage(String title, String message, MessageType messageType) {
        if (Module.nullCheck()) return;
        Messages messagesModule = Yeehaw.INSTANCE.moduleManager.getModule(Messages.class);
        Notifications notificationsModule = Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class);
        if (Yeehaw.INSTANCE.moduleManager.getModule(Console.class).clientMessages.getValue()) {
            Yeehaw.INSTANCE.consoleGUI.messageHistory.add(message);
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) return;
        }
        if (notificationsModule.isEnabled()) {
            if (messageType.equals(MessageType.GENERAL) && !notificationsModule.generalMessages.getValue()) return;
            else if (messageType.equals(MessageType.INFO) && !notificationsModule.infoMessages.getValue()) return;
            else if (messageType.equals(MessageType.ERROR) && !notificationsModule.errorMessages.getValue()) return;
            else if (messageType.equals(MessageType.DEBUG) && !notificationsModule.debugMessages.getValue()) return;
            Yeehaw.INSTANCE.notificationManager.addNotification(title, message, System.currentTimeMillis());
        } else {
            if (messageType.equals(MessageType.GENERAL) && !messagesModule.generalMessages.getValue()) return;
            else if (messageType.equals(MessageType.INFO) && !messagesModule.infoMessages.getValue()) return;
            else if (messageType.equals(MessageType.ERROR) && !messagesModule.errorMessages.getValue()) return;
            else if (messageType.equals(MessageType.DEBUG) && !messagesModule.debugMessages.getValue()) return;
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
        }
    }

    public static void sendRemovableMessage(String title, String message, int messageId, MessageType messageType) {
        if (Module.nullCheck()) return;
        Messages messagesModule = Yeehaw.INSTANCE.moduleManager.getModule(Messages.class);
        Notifications notificationsModule = Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class);
        if (Yeehaw.INSTANCE.moduleManager.getModule(Console.class).clientMessages.getValue()) {
            Yeehaw.INSTANCE.consoleGUI.messageHistory.add(message);
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) return;
        }
        if (notificationsModule.isEnabled()) {
            if (messageType.equals(MessageType.GENERAL) && !notificationsModule.generalMessages.getValue()) return;
            else if (messageType.equals(MessageType.INFO) && !notificationsModule.infoMessages.getValue()) return;
            else if (messageType.equals(MessageType.ERROR) && !notificationsModule.errorMessages.getValue()) return;
            else if (messageType.equals(MessageType.DEBUG) && !notificationsModule.debugMessages.getValue()) return;
            Yeehaw.INSTANCE.notificationManager.addNotification(title, message, System.currentTimeMillis());
        } else {
            if (messageType.equals(MessageType.GENERAL) && !messagesModule.generalMessages.getValue()) return;
            else if (messageType.equals(MessageType.INFO) && !messagesModule.infoMessages.getValue()) return;
            else if (messageType.equals(MessageType.ERROR) && !messagesModule.errorMessages.getValue()) return;
            else if (messageType.equals(MessageType.DEBUG) && !messagesModule.debugMessages.getValue()) return;
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(clientPrefix + message), messageId);
        }
    }

    public enum MessageType {
        GENERAL,
        INFO,
        ERROR,
        DEBUG,
        CONSTANT
    }
}
