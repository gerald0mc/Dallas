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
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectionComponent extends AbstractContainer {
    public boolean inClickGUI = false;
    int consoleX = width + 2;
    public boolean inConsoleGUI = false;

    public List<GuiScreen> guis;

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
        Gui.drawRect(consoleX, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 2, y + height, !inConsoleGUI ? new Color(0, 0, 0, 125).getRGB() : ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("ConsoleGUI", consoleX +  1, y + 1, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInsideClick(mouseX, mouseY)) {
            if(Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Error", "Already in ClickGUI.", true);
                return;
            }
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = true;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = true;
            Minecraft.getMinecraft().displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        } else if(isInsideConsole(mouseX, mouseY)) {
            if(Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) {
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
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException { }

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
