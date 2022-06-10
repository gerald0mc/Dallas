package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;

public class AFKMend extends Module {
    public AFKMend() {
        super("AFKMend", Category.MISC, "description");
    }

    public NumberSetting durability = new NumberSetting("Durability", 90, 1, 99, "Durability to switch to another item.");

    public int itemSlot = -1;

    @Override
    public String getMetaData() {
        ItemStack stack = new ItemStack(mc.player.getHeldItemOffhand().getItem());
        return itemSlot != -1 ? stack.getDisplayName() : "";
    }

    @Override
    public void onDisable() {
        if (itemSlot != -1)
            itemSlot = -1;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (mc.player.getHeldItemOffhand().getItem().equals(Items.AIR) || itemSlot == -1) {
            for (int i = 0; i < 45; ++i) {
                ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                    if (entry.getKey().equals(Enchantments.MENDING)) {
                        int dura = Math.round(((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage());
                        if (dura <= durability.getValue()) {
                            itemSlot = i;
                            InventoryUtil.moveItemToSlot(45, itemSlot);
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "Switched to new item for AFK mending.", MessageUtil.MessageType.INFO);
                            return;
                        }
                    }
                }
            }
            MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "No items to mend toggling module.", MessageUtil.MessageType.INFO);
            toggle();
        } else {
            ItemStack offHandStack = mc.player.getHeldItemOffhand();
            int offHandDura = Math.round(((offHandStack.getMaxDamage() - offHandStack.getItemDamage()) * 100f) / (float) offHandStack.getMaxDamage());
            if (offHandDura >= durability.getValue()) {
                for (int i = 0; i < 45; ++i) {
                    ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
                    for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                        if (entry.getKey().equals(Enchantments.MENDING)) {
                            int dura = Math.round(((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage());
                            if (dura <= durability.getValue()) {
                                itemSlot = i;
                                InventoryUtil.moveItemToSlot(45, itemSlot);
                                MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "Switched to new item for AFK mending.", MessageUtil.MessageType.INFO);
                                return;
                            }
                        }
                    }
                }
                MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "No items left to mend toggling module.", MessageUtil.MessageType.INFO);
                toggle();
            }
        }
    }
}
