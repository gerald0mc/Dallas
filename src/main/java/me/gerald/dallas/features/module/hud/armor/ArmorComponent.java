package me.gerald.dallas.features.module.hud.armor;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ArmorComponent extends HUDContainer {
    public ArmorComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        switch (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).alignment.getMode()) {
            case "Sideways":
                width = 80;
                height = 20;
                int xOffset = 0;
                for (ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList()) {
                    RenderUtil.renderItem(stack, x + xOffset, y);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).stackCount.getValue() ? (InventoryUtil.getTotalAmountOfItem(stack.getItem())) == 0 ? "" : String.valueOf(InventoryUtil.getTotalAmountOfItem(stack.getItem())) : "", x + xOffset + 12, y + 12, -1);
                    if (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).reverse.getValue())
                        xOffset += 20;
                    else
                        xOffset -= 20;
                }
                break;
            case "Vertical":
                height = 80;
                width = 20;
                int yOffset = 0;
                for (ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList()) {
                    RenderUtil.renderItem(stack, x, y + yOffset);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).stackCount.getValue() ? (InventoryUtil.getTotalAmountOfItem(stack.getItem())) == 0 ? "" : String.valueOf(InventoryUtil.getTotalAmountOfItem(stack.getItem())) : "", x + 12, y + yOffset + 12, -1);
                    if (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).reverse.getValue())
                        yOffset += 20;
                    else
                        yOffset -= 20;
                }
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
