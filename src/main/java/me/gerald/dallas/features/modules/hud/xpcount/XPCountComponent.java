package me.gerald.dallas.features.modules.hud.xpcount;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class XPCountComponent extends HUDContainer {
    public XPCountComponent(int x, int y, int width, int height) {
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
        switch (Yeehaw.INSTANCE.moduleManager.getModule(XPCount.class).renderMode.getMode()) {
            case "Item":
                width = 12;
                height = 12;
                RenderUtil.renderItem(new ItemStack(Items.EXPERIENCE_BOTTLE), x, y);
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) + (Yeehaw.INSTANCE.moduleManager.getModule(XPCount.class).stackCount.getValue() ? " " + InventoryUtil.getStackTotal(InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE)) : ""), x + 10, y + 12, -1);
                break;
            case "Name":
                width = Minecraft.getMinecraft().fontRenderer.getStringWidth("XP: " + InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) + (Yeehaw.INSTANCE.moduleManager.getModule(XPCount.class).stackCount.getValue() ? " " + InventoryUtil.getStackTotal(InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE)) : ""));
                height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("XP" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE) + (Yeehaw.INSTANCE.moduleManager.getModule(XPCount.class).stackCount.getValue() ? " " + InventoryUtil.getStackTotal(InventoryUtil.getTotalAmountOfItem(Items.EXPERIENCE_BOTTLE)) : ""), x, y, color.getRGB());
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
}
