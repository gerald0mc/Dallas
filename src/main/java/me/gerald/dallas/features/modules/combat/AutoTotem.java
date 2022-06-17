package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", Category.COMBAT, "Offhand but faster because only totem.");
    }

    public BooleanSetting message = new BooleanSetting("Message", true, "Toggles sending a message when switching a totem.");

    public boolean popped = false;

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (event.getEntity().equals(mc.player))
            popped = true;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING) || popped) {
            if (popped)
                popped = false;
            moveTotem();
        }
    }

    public void moveTotem() {
        int totemSlot = InventoryUtil.getItemInventory(Items.TOTEM_OF_UNDYING, true);
        if (totemSlot != -1) {
            InventoryUtil.moveItemToSlot(45, totemSlot);
            if (message.getValue())
                MessageUtil.sendMessage(ChatFormatting.BOLD + "AutoTotem", "Moved " + ChatFormatting.AQUA + "Totem " + ChatFormatting.RESET + "to hand.", MessageUtil.MessageType.CONSTANT);
        }
    }
}
