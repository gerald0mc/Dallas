package me.gerald.dallas.features.modules.hud.watermark;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class WatermarkComponent extends HUDContainer {
    public WatermarkComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Color color;
        if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
            color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
        } else {
            color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
        }
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(Yeehaw.MOD_NAME + " " + Yeehaw.VERSION);
        height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.MOD_NAME + " " + ChatFormatting.WHITE + Yeehaw.VERSION, x, y, color.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
    }

    @Override
    public int getHeight() {
        return height;
    }
}
