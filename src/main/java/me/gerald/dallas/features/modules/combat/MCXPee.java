package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class MCXPee extends Module {
    public MCXPee() {
        super("MCXPee", Category.COMBAT, "XP stuff.");
    }

    public BooleanSetting silent = new BooleanSetting("Silent", true);
    public NumberSetting testTimeChanger = new NumberSetting("TestTimeChanger", 1, 0.1f, 2);

    public boolean switched = false;
    public boolean silentlyPeeing = false;
    public int originalSlot = -1;

    @Override
    public void onDisable() {
        if(switched) switched = false;
        if(silentlyPeeing) silentlyPeeing = false;
        if(originalSlot != -1) originalSlot = -1;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        boolean peeing = Mouse.isButtonDown(2);
        if(peeing) {
            if(!mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE) && !switched) {
                int xpSlot = InventoryUtil.getItemHotbar(Items.EXPERIENCE_BOTTLE);
                if(xpSlot == -1) {
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "MCXPee", "You have no XP toggling...", true);
                    toggle();
                    return;
                }
                if(xpSlot != mc.player.inventory.currentItem) {
                    if(silent.getValue()) {
                        InventoryUtil.silentSwitchToSlot(xpSlot);
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "MCXPee", "Silent switched to XP.", true);
                        silentlyPeeing = true;
                    } else {
                        originalSlot = mc.player.inventory.currentItem;
                        InventoryUtil.switchToSlot(xpSlot);
                        MessageUtil.sendMessage(ChatFormatting.BOLD + "MCXPee", "Normal switched to XP.", true);
                    }
                    switched = true;
                    return;
                }
            }
            if(mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE) || silentlyPeeing) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        } else {
            if(switched) {
                if(silentlyPeeing) {
                    InventoryUtil.silentSwitchToSlot(mc.player.inventory.currentItem);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "MCXPee", "Silent switched to original slot.", true);
                    silentlyPeeing = false;
                } else {
                    if(mc.player.inventory.currentItem == originalSlot) return;
                    InventoryUtil.switchToSlot(originalSlot);
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "MCXPee", "Normal switched to original slot.", true);
                    originalSlot = -1;
                }
                switched = false;
            }
        }
    }
}
