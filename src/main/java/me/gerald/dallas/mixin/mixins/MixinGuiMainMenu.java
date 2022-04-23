package me.gerald.dallas.mixin.mixins;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.utils.ChangeConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;
import java.util.*;

@Mixin({GuiMainMenu.class})
public class MixinGuiMainMenu extends GuiScreen {
    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        List<ChangeConstructor> changelog = new ArrayList<>();
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "Search Box"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "FakePlayer", "Moving"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "WebhookSpammer", "Crasher Mode"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "MCP"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.COMMAND, "Emoji"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "Emojis"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "NameChanger", "FakeClip"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "Console"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "NameChanger"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "ArrayList"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.COMMAND, "Toggle"));
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " v" + ChatFormatting.WHITE + Yeehaw.VERSION, 1, 1, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Texas on TOP!", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1, -1);
        int yOffset = 0;
        Gui.drawRect(0, 18, 2 + getLongestWord(changelog), 29 + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * changelog.size()), new Color(0, 0, 0, 180).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Changelog" + ChatFormatting.GRAY + ":", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 2 + 1, -1);
        for (ChangeConstructor s : changelog) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s.getFullLog(), 1, 28 + yOffset, -1);
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
    }

    public int getLongestWord(List<ChangeConstructor> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (ChangeConstructor s : strings)
            hashMap.put(s.getFullLog(), Minecraft.getMinecraft().fontRenderer.getStringWidth(s.getFullLog()));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }
}
