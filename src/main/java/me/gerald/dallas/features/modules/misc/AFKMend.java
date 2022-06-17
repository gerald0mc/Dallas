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
        if (itemSlot == -1 || mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
            for (int i = 0; i < 45; ++i) {
                ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
                if (stack.getEnchantmentTagList().isEmpty()) continue;
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                    if (entry.getKey().equals(Enchantments.MENDING)) {
                        float dura = ((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage();
                        if (dura != 100) {
                            itemSlot = i;
                            InventoryUtil.moveItemToSlot(45, itemSlot);
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "Switched to new item for AFK mending.", MessageUtil.MessageType.INFO);
                            return;
                        }
                    }
                }
            }
        } else {
            ItemStack offHandStack = mc.player.getHeldItemOffhand();
            float offHandDura = ((offHandStack.getMaxDamage() - offHandStack.getItemDamage()) * 100f) / (float) offHandStack.getMaxDamage();
            if (offHandDura != 100)  return;
            for (int i = 0; i < 45; ++i) {
                ItemStack stack = Minecraft.getMinecraft().player.inventoryContainer.getInventory().get(i);
                if (stack.getEnchantmentTagList().isEmpty()) continue;
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(stack).entrySet()) {
                    if (entry.getKey().equals(Enchantments.MENDING)) {
                        float dura = ((stack.getMaxDamage() - stack.getItemDamage()) * 100f) / (float) stack.getMaxDamage();
                        if (dura != 100) {
                            itemSlot = i;
                            InventoryUtil.moveItemToSlot(45, itemSlot);
                            MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "Switched to new item for AFK mending.", MessageUtil.MessageType.INFO);
                            return;
                        }
                    }
                }
            }
            itemSlot = -1;
        }
        MessageUtil.sendMessage(ChatFormatting.BOLD + "AFKMend", "No items left to mend toggling module.", MessageUtil.MessageType.INFO);
        toggle();
    }
}
