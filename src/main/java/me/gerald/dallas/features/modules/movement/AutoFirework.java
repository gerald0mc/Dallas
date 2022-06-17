package me.gerald.dallas.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoFirework extends Module {
    public AutoFirework() {
        super("AutoFirework", Category.MOVEMENT, "When you reach a certain level while on the highway on flying it will use a firework.");
    }

    public NumberSetting heightToFirework = new NumberSetting("HeightToFirework", 1.5f, 0, 2, "Height above 120 to activate firework.");
    public NumberSetting delay = new NumberSetting("Delay", 1, 0, 5, "Amount in seconds to use another firework.");

    public TimerUtil delayTimer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!wearingElytra()) return;
        if (!mc.player.isElytraFlying()) return;
        if (mc.player.dimension != -1) return;
        if (mc.player.posY < 120) return;
        if (!delayTimer.passedMs((long) (delay.getValue() * 1000))) return;
        if (mc.player.posY <= 120 + heightToFirework.getValue()) {
            if (!mc.player.getHeldItemMainhand().getItem().equals(Items.FIREWORKS)) {
                int fireworkSlot = InventoryUtil.getItemHotbar(Items.FIREWORKS);
                if (fireworkSlot == -1) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "AutoFirework", "No fireworks in hotbar toggling...", MessageUtil.MessageType.INFO);
                    toggle();
                    return;
                }
                InventoryUtil.switchToSlot(fireworkSlot);
            }
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            delayTimer.reset();
        }
    }

    public boolean wearingElytra() {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.getItem().equals(Items.ELYTRA)) return true;
        }
        return false;
    }
}
