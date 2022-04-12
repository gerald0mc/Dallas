package me.gerald.dallas.features.module.hud.coordinates;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.module.client.GUI;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CoordinatesComponent extends HUDContainer {
    public CoordinatesComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        if(Minecraft.getMinecraft().player.dimension == 0 && Yeehaw.INSTANCE.moduleManager.getModule(Coordinates.class).nether.getValue()) {
            this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("X: " + (int) Minecraft.getMinecraft().player.posX + " Y: " + (int) Minecraft.getMinecraft().player.posY + " Z: " + (int) Minecraft.getMinecraft().player.posZ + " [" + ((int) Minecraft.getMinecraft().player.posX / 8) + " " + (int) Minecraft.getMinecraft().player.posY + " " + ((int) Minecraft.getMinecraft().player.posZ / 8) + "]");
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("X" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posX + ChatFormatting.RESET + " Y" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posY + ChatFormatting.RESET + " Z" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posZ + ChatFormatting.RED + " [" + ChatFormatting.GRAY + ((int) Minecraft.getMinecraft().player.posX / 8) + " " + (int) Minecraft.getMinecraft().player.posY + " " + ((int) Minecraft.getMinecraft().player.posZ / 8) + ChatFormatting.RED + "]", x, y, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        }else {
            this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("X: " + (int) Minecraft.getMinecraft().player.posX + " Y: " + (int) Minecraft.getMinecraft().player.posY + " Z: " + (int) Minecraft.getMinecraft().player.posZ);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("X" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posX + ChatFormatting.RESET + " Y" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posY + ChatFormatting.RESET + " Z" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + (int) Minecraft.getMinecraft().player.posZ, x, y, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {}

    @Override
    public int getHeight() {
        return height;
    }
}
