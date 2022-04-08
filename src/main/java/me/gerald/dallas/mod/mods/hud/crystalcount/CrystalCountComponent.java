package me.gerald.dallas.mod.mods.hud.crystalcount;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.HUDContainer;
import net.minecraft.client.Minecraft;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CrystalCountComponent extends HUDContainer {
    public CrystalCountComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        Yeehaw.INSTANCE.cpsManager.calculateCPS();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("CPS: " + Yeehaw.INSTANCE.cpsManager.getCps());
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("CPS: " + Yeehaw.INSTANCE.cpsManager.getCps(), x, y, -1);
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
