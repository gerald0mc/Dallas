package me.gerald.dallas.features.gui.api;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.utils.Globals;
import net.minecraft.client.Minecraft;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public abstract class AbstractContainer implements Globals {
    public int x, y, width, height;

    public AbstractContainer(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);

    public abstract void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException;

    public abstract int getHeight();

    public boolean isInside(int mouseX, int mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public boolean isInside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public String trimValue(String preValue, String value, String otherValue) {
        int otherWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(otherValue);
        int maxWidth = width - 2 - otherWidth;
        if (Minecraft.getMinecraft().fontRenderer.getStringWidth(value) < maxWidth) return preValue + value;
        else {
            int preWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(preValue);
            int dotWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth("...");
            if(this instanceof ModuleContainer) {
                ModuleContainer container = (ModuleContainer) this;
                container.needsHover = true;
            }
            return preValue + Minecraft.getMinecraft().fontRenderer.trimStringToWidth(value, maxWidth - preWidth - dotWidth) + "...";
        }
    }
}
