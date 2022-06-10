package me.gerald.dallas.features.gui.comps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.gui.console.ConsoleGUI;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.RenderUtil;
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
    int consoleX = width + 8;

    public SelectionComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        guis = new ArrayList<>();
        guis.add(Yeehaw.INSTANCE.clickGUI);
        guis.add(Yeehaw.INSTANCE.consoleGUI);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //clickGUI tab
        Gui.drawRect(x - 2, y, width + 4, y + height, !inClickGUI ? new Color(0, 0, 0, 125).getRGB() : ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("ClickGUI", x + 2, y + 3, -1);
        RenderUtil.renderBorder(x - 2, y, width + 4, y + height, 1, new Color(0, 0, 0, 255));

        //console tab
        Gui.drawRect(consoleX - 2, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 6, y + height, !inConsoleGUI ? new Color(0, 0, 0, 125).getRGB() : ClickGUI.clientColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("ConsoleGUI", consoleX + 2, y + 3, -1);
        RenderUtil.renderBorder(consoleX - 2, y, consoleX + Minecraft.getMinecraft().fontRenderer.getStringWidth("ConsoleGUI") + 6, y + height, 1, new Color(0, 0, 0, 255));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInsideClick(mouseX, mouseY)) {
            if (Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Error", "Already in ClickGUI.", MessageUtil.MessageType.ERROR);
                return;
            }
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.consoleGUI.selectionBox.inClickGUI = true;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inConsoleGUI = false;
            Yeehaw.INSTANCE.clickGUI.selectionBox.inClickGUI = true;
            Minecraft.getMinecraft().displayGuiScreen(Yeehaw.INSTANCE.clickGUI);
        } else if (isInsideConsole(mouseX, mouseY)) {
            if (Minecraft.getMinecraft().currentScreen instanceof ConsoleGUI) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Error", "Already in ConsoleGUI.", MessageUtil.MessageType.ERROR);
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
