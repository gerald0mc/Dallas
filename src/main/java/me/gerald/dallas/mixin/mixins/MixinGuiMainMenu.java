package me.gerald.dallas.mixin.mixins;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.utils.ChangeConstructor;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;
import java.util.*;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {
    @Shadow
    private String splashText;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void postConstructor(final CallbackInfo ci) {
        this.splashText = getRandomSplash(Yeehaw.INSTANCE.splashText);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        //change log
        List<ChangeConstructor> changelog = new ArrayList<>();
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE_ADD, "Offhand", "AbsorptionAdd"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.COMMAND, "Splash"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.CHANGE, "Chat Module Name", "ChatPrefix"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.FIX, "Emojis and ChatPrefix not working together"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "Runescape Chat (WIP Does nothing ATM!)"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE_ADD, "Armor", "Durability"));
        changelog.add(new ChangeConstructor(ChangeConstructor.ChangeType.MODULE, "Viewmodel", "Render"));
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " v" + ChatFormatting.WHITE + Yeehaw.VERSION, 1, 1, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Texas on TOP!", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1, -1);
        int yOffsetChange = 0;
        Gui.drawRect(0, 18, 2 + getLongestWord1(changelog), 29 + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * changelog.size()), new Color(0, 0, 0, 180).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Changelog" + ChatFormatting.GRAY + ":", 1, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 2 + 1, -1);
        for (ChangeConstructor s : changelog) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s.getFullLog(), 1, 28 + yOffsetChange, -1);
            yOffsetChange += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
        RenderUtil.renderBorder(0, 18, 2 + getLongestWord1(changelog), 29 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * changelog.size(), 1, new Color(0, 0, 0, 255));
        //dev log
        List<String> devLog = new ArrayList<>();
        devLog.add("Module's NameChanger and RunescapeChat are causing issues don't use.");
        devLog.add("Module's PacketLog and Chams crash don't use.");
        devLog.add("Module Strafe is busted don't use.");
        int yOffsetDev = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * 3 + yOffsetChange + 3;
        int devY = yOffsetDev;
        Gui.drawRect(0, devY, 2 + getLongestWord2(devLog), yOffsetDev + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * devLog.size()) + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, new Color(0, 0, 0, 180).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Dev Log" + ChatFormatting.GRAY + ":", 1, devY + 1, -1);
        yOffsetDev += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        for (String s : devLog) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, 1, yOffsetDev, -1);
            yOffsetDev += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }
        RenderUtil.renderBorder(0, devY, 2 + getLongestWord2(devLog), devY + (Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * devLog.size() + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT), 1, new Color(0, 0, 0, 255));
    }

    public int getLongestWord1(List<ChangeConstructor> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (ChangeConstructor s : strings)
            hashMap.put(s.getFullLog(), Minecraft.getMinecraft().fontRenderer.getStringWidth(s.getFullLog()));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }

    public int getLongestWord2(List<String> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }

    public String getRandomSplash(List<String> stringList) {
        Random r = new Random();
        return stringList.get(r.nextInt(stringList.size() - 1));
    }
}
