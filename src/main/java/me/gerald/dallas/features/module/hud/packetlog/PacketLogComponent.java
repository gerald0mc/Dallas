package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketLogComponent extends HUDContainer {
    public static List<String> packetHistory;
    public int page = 0;

    public PacketLogComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.height = 150;
        // This isn't ever more than 100 elements, performance hit is negligible
        packetHistory = new CopyOnWriteArrayList<>();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final PacketLog module = Yeehaw.INSTANCE.moduleManager.getModule(PacketLog.class);
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        //logger
        Gui.drawRect(x, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 2, y, page == 0 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Log", x + 1, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
        //selection
        Gui.drawRect(x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 2, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, (x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 2) + Minecraft.getMinecraft().fontRenderer.getStringWidth("Selection") + 2, y, page == 1 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Selection", x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 4, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
        switch (page) {
            case 0:
                while (packetHistory.size() >= module.max.getValue()) packetHistory.remove(0);
                int longest = getLongestWord(packetHistory);
                width = longest > 300 ? longest + 3 : 300;
                Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 175).getRGB());
                //full box
                //top lines
                Gui.drawRect(x, y, x + width, y + 1, new Color(0, 0, 0, 255).getRGB());
                //left line
                Gui.drawRect(x, y, x + 1, y + height, new Color(0, 0, 0, 255).getRGB());
                //right line
                Gui.drawRect(x + width - 1, y, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
                //bottom line
                Gui.drawRect(x, y + height, x + width, y + height + 1, new Color(0, 0, 0, 255).getRGB());
                int yOffset = 0;
                if (packetHistory.isEmpty()) return;
                for (String s : packetHistory) {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, x + 2, y + 2 + yOffset, -1);
                    yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                }
                break;
            case 1:
                //PacketLog.settings.forEach();
                // i dont wanna do this lol
                break;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
    }

    @Override
    public int getHeight() {
        return height;
    }

    public int getLongestWord(List<String> strings) {
        return strings.stream().mapToInt(mc.fontRenderer::getStringWidth).max().orElse(0);
    }

    public boolean isInside(int mouseX, int mouseY, int x, int y) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
