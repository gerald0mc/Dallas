package me.gerald.dallas.mod.mods.hud.watermark;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.HUDContainer;
import net.minecraft.client.Minecraft;

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
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(Yeehaw.MOD_NAME);
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.MOD_NAME, x, y, -1);
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
