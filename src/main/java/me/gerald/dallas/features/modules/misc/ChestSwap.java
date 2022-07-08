package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class ChestSwap extends Module {
    public ChestSwap() {
        super("ChestSaver", Category.MISC, "Swaps your ChestPlate to a Elytra and vice versa.");
    }

    private final Item[] chestPieces = {Items.DIAMOND_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.LEATHER_CHESTPLATE};

    @Override
    public void onEnable() {
        if (nullCheck()) return;
        if (mc.player.inventory.getStackInSlot(6).getItem().equals(Items.ELYTRA)) {
            for (Item item : chestPieces) {
                attemptSwitch(item);
            }
            MessageUtil.sendMessage(ChatFormatting.BOLD + "ChestSwap", "No ChestPlate's, now toggling...", MessageUtil.MessageType.INFO);
        } else if (mc.player.inventory.getStackInSlot(6).getItem() instanceof ItemArmor) {
            if (!attemptSwitch(Items.ELYTRA)) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "ChestSwap", "No Elytra to switch to, now toggling...", MessageUtil.MessageType.INFO);
            }
        } else if (mc.player.inventory.getStackInSlot(6).getItem().equals(Items.AIR)) {
            for (Item item : chestPieces) {
                attemptSwitch(item);
            }
            MessageUtil.sendMessage(ChatFormatting.BOLD + "ChestSwap", "No ChestPlate's, now toggling...", MessageUtil.MessageType.INFO);
        }
        toggle();
    }

    public boolean attemptSwitch(Item item) {
        int slot = InventoryUtil.getItemInventory(item, false);
        if (slot == -1) return false;
        InventoryUtil.moveItemToSlot(6, slot);
        MessageUtil.sendMessage(ChatFormatting.BOLD + "ChestSwap", "Swapped to new armor, now toggling...", MessageUtil.MessageType.INFO);
        return true;
    }
}
