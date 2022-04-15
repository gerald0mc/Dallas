package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiTrap extends Module {
    public BooleanSetting fullAnti = register(new BooleanSetting("FullAnti", true));
    public BooleanSetting alwaysActive = register(new BooleanSetting("AlwaysActive", false));
    public NumberSetting distanceToActivate = register(new NumberSetting("DistanceToAct", 10, 0, 30));
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
        if (player != null && mc.player.getDistance(player) > distanceToActivate.getValue() && !alwaysActive.getValue())
            return;
        if (player == null && !alwaysActive.getValue()) return;
        if (targetPos != null && fullAnti.getValue()) {
            BlockUtil.placeBlock(targetPos, true, Items.END_CRYSTAL);
        } else if (preTrapPos != null) {
            BlockUtil.placeBlock(preTrapPos, true, Items.END_CRYSTAL);
        }
    }
}
