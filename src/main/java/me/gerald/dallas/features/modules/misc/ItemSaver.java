package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PlayerDamageBlockEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemSaver extends Module {
    public ItemSaver() {
        super("ItemSaver", Category.MISC, "Stops you from mining blocks with the item in your mainhand if below a certain durability.");
    }

    public NumberSetting dura = new NumberSetting("Durability", 10, 1, 100, "What durability percentage to cancel packets.");
    public BooleanSetting switchNewItem = new BooleanSetting("SwitchNewItem", true, "Toggles switching to a new item if your current one is hurt.");

    @SubscribeEvent
    public void onBlockInteract(PlayerDamageBlockEvent event) {
        ItemStack mainHandStack = mc.player.getHeldItemMainhand();
        if (mainHandStack.getItem().equals(Items.AIR) || !mainHandStack.isItemDamaged() || Math.round(((mainHandStack.getMaxDamage() - mainHandStack.getItemDamage()) * 100f) / (float) mainHandStack.getMaxDamage()) >= dura.getValue()) return;
        event.setCanceled(true);
        MessageUtil.sendMessage(ChatFormatting.BOLD + "ItemJesus", "Stopped you from losing durability.", MessageUtil.MessageType.INFO);
        if (switchNewItem.getValue()) {
            Item item = mc.player.getHeldItemMainhand().getItem();
            int newSlot = InventoryUtil.getItemInventory(item, false);
            if (newSlot != -1) {
                InventoryUtil.moveItemToSlot(mc.player.inventory.currentItem, newSlot);
                MessageUtil.sendMessage(ChatFormatting.BOLD + "ItemJesus", "Moved new tool to hand.", MessageUtil.MessageType.INFO);
            }
        }
    }
}
