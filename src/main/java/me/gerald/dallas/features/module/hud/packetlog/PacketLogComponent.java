package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.gui.comps.settingcomps.BooleanComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacketLogComponent extends HUDContainer {
    public static List<String> packetHistory = new CopyOnWriteArrayList<>();
    public int page = 0;
    public int packetPage = 0;

    public int logTabX = 0;
    public int selectionTabX = 0;
    public int sPacketsTabX = 0;
    public int cPacketsTabX = 0;
    public int miscPacketsTabX = 0;

    public PacketLogComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.height = 154;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        final PacketLog module = Yeehaw.INSTANCE.moduleManager.getModule(PacketLog.class);
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        int tabXOffset = 0;
        //logger
        Gui.drawRect(x, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 2, y, page == 0 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Log", x + 1, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
        tabXOffset += Minecraft.getMinecraft().fontRenderer.getStringWidth("Log") + 4;
        //selection
        Gui.drawRect(x + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + tabXOffset + Minecraft.getMinecraft().fontRenderer.getStringWidth("Selection") + 2, y, page == 1 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Selection", x + tabXOffset + 2, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
        if(selectionTabX != x + tabXOffset) selectionTabX = x + tabXOffset;
        tabXOffset += Minecraft.getMinecraft().fontRenderer.getStringWidth("Selection") + 4;
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
                //SPackets
                Gui.drawRect(x + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("SPackets") + 2 + tabXOffset, y, packetPage == 0 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("SPackets", x + 2 + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
                if(sPacketsTabX != x + tabXOffset) sPacketsTabX = x + tabXOffset;
                tabXOffset += Minecraft.getMinecraft().fontRenderer.getStringWidth("SPackets") + 4;
                //CPackets
                Gui.drawRect(x + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("CPackets") + 2 + tabXOffset, y, packetPage == 1 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("CPackets", x + 2 + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
                if(cPacketsTabX != x + tabXOffset) cPacketsTabX = x + tabXOffset;
                tabXOffset += Minecraft.getMinecraft().fontRenderer.getStringWidth("CPackets") + 4;
                //MiscPackets
                Gui.drawRect(x + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("MiscPackets") + 2 + tabXOffset, y, packetPage == 2 ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 175).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("MiscPackets", x + 2 + tabXOffset, y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, -1);
                if(miscPacketsTabX != x + tabXOffset) miscPacketsTabX = x + tabXOffset;
                switch (packetPage) {
                    case 0:
                        int yOffset2 = y;
                        int xOffset = 0;
                        int amountInRow = 0;
                        for(BooleanComponent component : PacketLog.sPacketSettings) {
                            if(this.width != x + xOffset) this.width = xOffset;
                            component.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2;
                            component.width = 100;
                            if(amountInRow == 14) {
                                amountInRow = 0;
                                xOffset += component.width;
                                yOffset2 = y;
                            }
                            component.x = x + xOffset;
                            component.y = yOffset2;
                            component.drawScreen(mouseX, mouseY, partialTicks);
                            amountInRow++;
                            yOffset2 += component.height;
                        }
                        break;
                    case 1:
                        int yOffset3 = y;
                        int xOffset2 = 0;
                        int amountInRow2 = 0;
                        for(BooleanComponent component : PacketLog.cPacketSettings) {
                            if(this.width != x + xOffset2) this.width = xOffset2;
                            component.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2;
                            component.width = 100;
                            if(amountInRow2 == 14) {
                                amountInRow2 = 0;
                                xOffset2 += component.width;
                                yOffset3 = y;
                            }
                            component.x = x + xOffset2;
                            component.y = yOffset3;
                            component.drawScreen(mouseX, mouseY, partialTicks);
                            amountInRow2++;
                            yOffset3 += component.height;
                        }
                        break;
                    case 2:
                        int yOffset4 = y;
                        int xOffset3 = 0;
                        int amountInRow3 = 0;
                        for(BooleanComponent component : PacketLog.miscPacketSettings) {
                            if(this.width != x + xOffset3) this.width = xOffset3;
                            component.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 2;
                            component.width = 100;
                            if(amountInRow3 == 14) {
                                amountInRow3 = 0;
                                xOffset3 += component.width;
                                yOffset4 = y;
                            }
                            component.x = x + xOffset3;
                            component.y = yOffset4;
                            component.drawScreen(mouseX, mouseY, partialTicks);
                            amountInRow3++;
                            yOffset4 += component.height;
                        }
                        break;
                }
                //PacketLog.settings.forEach();
                // i dont wanna do this lol
                break;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //log tab
        if(isInside(mouseX, mouseY, logTabX, y, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("Log"), y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT)) {
            if(mouseButton == 0) {
                page = 0;
            }
        //selection tab
        }else if(isInside(mouseX, mouseY, selectionTabX, y, selectionTabX + Minecraft.getMinecraft().fontRenderer.getStringWidth("Selection"), y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT)) {
            if(mouseButton == 0) {
                page = 1;
            }
        }
        if(page == 1) {
            //spacket tab
            if(isInside(mouseX, mouseY, sPacketsTabX, y, sPacketsTabX + Minecraft.getMinecraft().fontRenderer.getStringWidth("SPackets"), y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT)) {
                if(mouseButton == 0) {
                    packetPage = 0;
                }
            //cpacket tab
            }else if(isInside(mouseX, mouseY, cPacketsTabX, y, cPacketsTabX + Minecraft.getMinecraft().fontRenderer.getStringWidth("CPackets"), y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT)) {
                if(mouseButton == 0) {
                    packetPage = 1;
                }
            //miscpacket tab
            }else if(isInside(mouseX, mouseY, miscPacketsTabX, y, miscPacketsTabX + Minecraft.getMinecraft().fontRenderer.getStringWidth("MiscPackets"), y - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT)) {
                if(mouseButton == 0) {
                    packetPage = 2;
                }
            }
        }
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                beginDragging(mouseX, mouseY);
            }
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

    public boolean isInside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }
}
