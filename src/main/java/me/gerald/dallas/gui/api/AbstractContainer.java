package me.gerald.dallas.gui.api;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public abstract class AbstractContainer {
    public int x, y, width, height;
    public boolean last = false;

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
}
