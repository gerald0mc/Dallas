package me.gerald.dallas.features.modules.hud.entitylist;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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
        HashMap<String, Integer> entityNames = new LinkedHashMap<>();
        for(Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if(entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                entityNames.put(player.getDisplayNameString(), 1);
            } else {
                if (Yeehaw.INSTANCE.moduleManager.getModule(EntityList.class).playersOnly.getValue()) continue;
                if(entityNames.containsKey(entity.getName()))
                    entityNames.replace(entity.getName(), entityNames.get(entity.getName()) + 1);
                else
                    entityNames.put(entity.getName(), 1);
            }
        }
        int yOffset = 0;
        for(Map.Entry<String, Integer> entry : entityNames.entrySet()) {
            EntityPlayer player = mc.world.getPlayerEntityByName(entry.getKey());
            if(player != null)
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((int) (player.getHealth() + player.getAbsorptionAmount()) + " " + player.getDisplayNameString(), x, y + yOffset, -1);
            else
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.RED + "x" + entry.getValue() + ChatFormatting.RESET + " " + entry.getKey(), x, y + yOffset, -1);
            yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
        }
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(entityNames.size() == 0 ? "Whole lotta cock" : getLongestWord(entityNames));
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

    public String getLongestWord(HashMap<String, Integer> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : strings.entrySet())
            hashMap.put("x" + entry.getValue() + " " + entry.getKey(), Minecraft.getMinecraft().fontRenderer.getStringWidth("x" + entry.getValue() + " " + entry.getKey()));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }
}
