package me.gerald.dallas.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtils {
    public static int getItemHotbar(Item item) {
        for(int i = 0; i < 9; i++) {
            if(Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getItemInventory(Item item, boolean hotbar) {
        for(int i = (hotbar ? 0 : 9); i < 36; i++) {
            if(Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int getTotalAmountOfItem(Item item) {
        int amountOfItem = 0;
        for(int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if(stack.getItem() == item)
                amountOfItem += stack.getCount();
        }
        if(Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
            amountOfItem += Minecraft.getMinecraft().player.getHeldItemOffhand().getCount();
        return amountOfItem;
    }

    public static void moveItemToSlot(Integer startSlot, Integer endSlot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, endSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, startSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
    }

    public static void silentSwitchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().playerController.updateController();
    }

    public static void switchToSlot(int slot) {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketHeldItemChange(slot));
        Minecraft.getMinecraft().player.inventory.currentItem = slot;
        Minecraft.getMinecraft().playerController.updateController();
    }
}
