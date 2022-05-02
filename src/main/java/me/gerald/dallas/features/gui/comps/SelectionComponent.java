package me.gerald.dallas.features.gui.comps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.gui.console.ConsoleGUI;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectionComponent extends AbstractContainer {
    public boolean inClickGUI = false;
    public boolean inConsoleGUI = false;
    public List<GuiScreen> guis;
    int consoleX = width + 4;

    public SelectionComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        guis = new ArrayList<>();
        guis.add(Yeehaw.INSTANCE.clickGUI);
        guis.add(Yeehaw.INSTANCE.consoleGUI);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, width, y + height, !inClickGUI ? new Color(0, 0, 0, 125).getRGB() : ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("ClickGUI", x + 1, y + 1, -1);
        //top lines
        Gui.drawRect(x, y, width, y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(x, y, x - 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(width, y, width + 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(x - 1, y + height, width + 1, y + height + 1, new Color(0, 0, 0, 255).getRGB());
        Gui.drawRect(consoleX, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 2, y + height, !inConsoleGUI ? new Color(0, 0, 0, 125).getRGB() : ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("ConsoleGUI", consoleX + 1, y + 1, -1);
        //top lines
        Gui.drawRect(consoleX, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 2, y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(consoleX, y, consoleX - 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 2, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 3, y + height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(consoleX - 1, y + height, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 3, y + height + 1, new Color(0, 0, 0, 255).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInsideClick(mouseX, mouseY)) {
            if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Error", "Already in ClickGUI.", true);
                return;
            }
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = true;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = true;
            Minecraft.getMinecraft().displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        } else if (isInsideConsole(mouseX, mouseY)) {
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Error", "Already in ConsoleGUI.", true);
                return;
            }
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = true;
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = false;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = true;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = false;
            Minecraft.getMinecraft().displayGuiScreen(Yeehaw.INSTANCE.consoleGUI);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
    }

    public boolean isInsideClick(int mouseX, int mouseY) {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < y + height;
    }

    public boolean isInsideConsole(int mouseX, int mouseY) {
        return mouseX > consoleX && mouseX < consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 2 && mouseY > y && mouseY < y + height;
    }
}
