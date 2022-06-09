package me.gerald.dallas.features.modules.hud.armor;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
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
        if (Module.nullCheck()) return;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).alignment.getMode()) {
            case "Sideways":
                width = 80;
                height = 20;
                int xOffset = 0;
                for (ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList()) {
                    if (stack.getItem().equals(Items.AIR)) {
                        if (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).reverse.getValue())
                            xOffset += Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
                        else
                            xOffset -= Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
                        continue;
                    }
                    int duraString = Math.round(((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage());
                    RenderUtil.renderItem(stack, Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).stackCount.getValue() ? InventoryUtil.getTotalAmountOfItem(stack.getItem()) == 0 ? "" : String.valueOf(InventoryUtil.getTotalAmountOfItem(stack.getItem())) : "", x + xOffset, y);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).durability.getValue() ? String.valueOf(duraString) : "", x + xOffset - 1, y - 8, new Color(66, 226, 29).getRGB());
                    if (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).reverse.getValue())
                        xOffset += Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
                    else
                        xOffset -= Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
                }
                break;
            case "Vertical":
                height = 80;
                width = 20;
                int yOffset = 0;
                for (ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList()) {
                    RenderUtil.renderItem(stack, Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).stackCount.getValue() ? (InventoryUtil.getTotalAmountOfItem(stack.getItem())) == 0 ? "" : String.valueOf(InventoryUtil.getTotalAmountOfItem(stack.getItem())) : "", x, y + yOffset);
                    int duraString = Math.round(((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage());
                    RenderUtil.renderItem(stack, Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).stackCount.getValue() ? InventoryUtil.getTotalAmountOfItem(stack.getItem()) == 0 ? "" : String.valueOf(InventoryUtil.getTotalAmountOfItem(stack.getItem())) : "", x, y + yOffset);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).durability.getValue() ? String.valueOf(duraString) : "", x - 1, y + yOffset - 8, new Color(66, 226, 29).getRGB());
                    if (Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).reverse.getValue())
                        yOffset += Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
                    else
                        yOffset -= Yeehaw.INSTANCE.moduleManager.getModule(Armor.class).offset.getValue();
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
