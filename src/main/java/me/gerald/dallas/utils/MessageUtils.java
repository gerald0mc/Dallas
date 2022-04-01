package me.gerald.dallas.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class MessageUtils {
    public static String clientPrefix = ChatFormatting.DARK_GRAY + "<" + ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.DARK_GRAY + "> " + ChatFormatting.RESET;

    public static void sendMessage(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(clientPrefix + message));
    }
}
