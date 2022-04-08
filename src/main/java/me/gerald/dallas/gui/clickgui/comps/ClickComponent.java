package me.gerald.dallas.gui.clickgui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.mods.client.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClickComponent extends AbstractContainer {
    public String text;
    public boolean clicked = false;

    public ClickComponent(String text, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, !clicked ? new Color(0, 0, 0, 125).getRGB() :  new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x + 2, y + 2, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A click component with the text (" + text + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A click component with the text (" + text + ").") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                clicked = !clicked;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {}

    @Override
    public int getHeight() {
        return height;
    }
}
