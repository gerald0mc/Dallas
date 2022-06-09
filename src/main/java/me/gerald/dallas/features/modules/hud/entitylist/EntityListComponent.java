package me.gerald.dallas.features.modules.hud.entitylist;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.gui.api.HUDContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;

public class EntityListComponent extends HUDContainer {
    public EntityListComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        HashMap<Entity, Integer> entityNames = new LinkedHashMap<>();
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                entityNames.put(player, 1);
            } else {
                if (entityNames.containsKey(entity))
                    entityNames.replace(entity, entityNames.get(entity) + 1);
                else
                    entityNames.put(entity, 1);
            }
        }
        int yOffset = 0;
        for (Map.Entry<Entity, Integer> entry : entityNames.entrySet()) {
            if (entry.getKey() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entry.getKey();
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((int) (player.getHealth() + player.getAbsorptionAmount()) + " " + player.getDisplayNameString(), x, y + yOffset, -1);
            } else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.RED + "x" + entry.getValue() + ChatFormatting.RESET + " " + entry.getKey().getName(), x, y + yOffset, -1);
            }
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
        }
        width = Collections.max(entityNames.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
        height = yOffset == 0 ? Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT : yOffset;
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
