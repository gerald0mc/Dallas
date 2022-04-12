package me.gerald.dallas.features.module.hud.cps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.features.gui.api.HUDContainer;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CPSComponent extends HUDContainer {
    public CPSComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Yeehaw.INSTANCE.cpsManager.calculateCPS();
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("CPS: " + Yeehaw.INSTANCE.cpsManager.getCps());
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("CPS" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + Yeehaw.INSTANCE.cpsManager.getCps(), x, y, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {}

    @Override
    public int getHeight() {
        return height;
    }
}
