package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.hud.notification.Notifications;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
    public static String clientPrefix = ChatFormatting.DARK_GRAY + "<" + ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.DARK_GRAY + "> " + ChatFormatting.RESET;

    public static void sendMessage(String title, String message, boolean clientMessage) {
        if (Module.nullCheck()) return;
        if (Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).isEnabled()) {
            if (clientMessage && !Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).clientMessages.getValue())
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
            else
                Yeehaw.INSTANCE.notificationManager.addNotification(title, message, System.currentTimeMillis());
        } else {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
        }
    }
}
