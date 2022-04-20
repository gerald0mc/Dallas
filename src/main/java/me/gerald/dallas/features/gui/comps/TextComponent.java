package me.gerald.dallas.features.gui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.DragComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class TextComponent extends DragComponent {
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
        Gui.drawRect(x, y, x + width, y + height, ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x + 2, y + 2, -1);
        //borders
        //top lines
        Gui.drawRect(x, y, x + width, y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(x, y, x + 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(x + width - 1, y, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(x, y + height - 1, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) {
    }

    @Override
    public int getHeight() {
        return height;
    }
}
