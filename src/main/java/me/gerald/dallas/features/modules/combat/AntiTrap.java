package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiTrap extends Module {
    public BooleanSetting fullAnti = new BooleanSetting("FullAnti", true);
    public BooleanSetting alwaysActive = new BooleanSetting("AlwaysActive", false);
    public BooleanSetting autoSwitch = new BooleanSetting("AutoSwitch", true, () -> alwaysActive.getValue());
    public BooleanSetting noCrystalToggle = new BooleanSetting("NoCrystalToggle", true, () -> autoSwitch.getValue() && autoSwitch.isVisible());
    public NumberSetting distanceToActivate = new NumberSetting("DistanceToAct", 10, 0, 30);

    public AntiTrap() {
        super("AntiTrap", Category.COMBAT, "Places a crystal on the block next to you so you cannot be trapped.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (!BlockUtil.isSurrounded(playerPos)) return;
        BlockPos targetPos = BlockUtil.canPlaceCrystalSurround(playerPos);
        BlockPos preTrapPos = BlockUtil.isPreTrap(playerPos);
        EntityPlayer player = BlockUtil.findClosestPlayer();
        if (player != null && mc.player.getDistance(player) > distanceToActivate.getValue() && !alwaysActive.getValue()) return;
        else if (player == null && !alwaysActive.getValue()) return;
        int originalSlot = -1;
        if(autoSwitch.getValue() && autoSwitch.isVisible()) {
            int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
            if(crystalSlot == -1 && noCrystalToggle.getValue() && noCrystalToggle.isVisible()) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "AntiTrap", "No crystals in hotbar toggling module...", true);
                toggle();
                return;
            }
            originalSlot = mc.player.inventory.currentItem;
            InventoryUtil.switchToSlot(crystalSlot);
        }
        if (targetPos != null && fullAnti.getValue()) {
            BlockUtil.placeBlock(targetPos, true, Items.END_CRYSTAL);
        } else if (preTrapPos != null) {
            BlockUtil.placeBlock(preTrapPos, true, Items.END_CRYSTAL);
        }
        if(originalSlot != mc.player.inventory.currentItem && originalSlot != -1) {
            InventoryUtil.switchToSlot(originalSlot);
        }
    }
}
