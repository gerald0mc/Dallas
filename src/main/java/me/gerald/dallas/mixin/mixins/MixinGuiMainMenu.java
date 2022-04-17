package me.gerald.dallas.mixin.mixins;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Mixin({GuiMainMenu.class})
public class MixinGuiMainMenu extends GuiScreen {
    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        String[] changelog = new String[] {ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "ItemESP", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "Armor", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "Sprint", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "WebhookSpammer", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "Webhook" + ChatFormatting.RESET + " command", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "Waypoints", ChatFormatting.GRAY + "-" + ChatFormatting.RESET + "Added " + ChatFormatting.AQUA + "Waypoints " + ChatFormatting.RESET + "command"};
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " v" + ChatFormatting.WHITE + Yeehaw.VERSION, 1, 1, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Texas on TOP!", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1, -1);
        int yOffset = 0;
        Gui.drawRect(0, 18, 2 + getLongestWord(changelog), 29 + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * changelog.length), new Color(0, 0, 0, 180).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Changelog" + ChatFormatting.GRAY + ":", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 2 + 1, -1);
        for(String s : changelog) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, 1, 28 + yOffset, -1);
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
    }

    public int getLongestWord(String[] strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for(String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }
}
