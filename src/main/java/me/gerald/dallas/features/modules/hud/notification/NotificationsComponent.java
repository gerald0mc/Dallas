package me.gerald.dallas.features.modules.hud.notification;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.managers.notification.Notification;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class NotificationsComponent extends HUDContainer {
    public NotificationsComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Color color;
        if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
            color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
        } else {
            color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
        }
        if (Yeehaw.INSTANCE.notificationManager.notifications.isEmpty() || !Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).isEnabled())
            return;
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).title.getValue() ? "[" + Yeehaw.INSTANCE.notificationManager.notifications.peek().getTitle() + "]: " + Yeehaw.INSTANCE.notificationManager.notifications.peek().getMessage() : Yeehaw.INSTANCE.notificationManager.notifications.peek().getMessage() + 4);
        height = 12;
        int yOffset = 0;
        for (Notification notificationConstructor : Yeehaw.INSTANCE.notificationManager.getNotifications()) {
            if (!Yeehaw.INSTANCE.moduleManager.getModule(Notifications.class).title.getValue()) {
                Gui.drawRect(x, y + yOffset, x + Minecraft.getMinecraft().fontRenderer.getStringWidth(notificationConstructor.getMessage() + 6), y + height + yOffset, new Color(0, 0, 0, 170).getRGB());
                Gui.drawRect(x, y + yOffset, x + 2, y + height + yOffset, color.getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notificationConstructor.getMessage(), x + 4, y + 2 + yOffset, -1);
            } else {
                Gui.drawRect(x, y + yOffset, x + Minecraft.getMinecraft().fontRenderer.getStringWidth("[" + notificationConstructor.getTitle() + "]: "  + notificationConstructor.getMessage()), y + height + yOffset, new Color(0, 0, 0, 170).getRGB());
                Gui.drawRect(x, y + yOffset, x + 2, y + height + yOffset, color.getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + "[" + ChatFormatting.RESET + notificationConstructor.getTitle() + ChatFormatting.GRAY + "]: " + ChatFormatting.RESET + notificationConstructor.getMessage(), x + 4, y + 2 + yOffset, -1);
            }
            yOffset += height + 2;
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
}
