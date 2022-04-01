package me.gerald.dallas.gui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.mods.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class TextComponent extends AbstractContainer {
    public String text;

    public TextComponent(String text, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x + 2, y + 2, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void keyTyped(char keyChar, int key) {}

    @Override
    public int getHeight() {
        return height;
    }
}
