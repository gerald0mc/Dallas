package me.gerald.dallas.features.modules.combat;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CevPlace extends Module {
    public NumberSetting range = new NumberSetting("Range", 4, 1, 6);
    public NumberSetting delay = new NumberSetting("Delay", 100, 0, 2000);
    public BooleanSetting alwaysActive = new BooleanSetting("AlwaysActive", false);
    public BooleanSetting ownSwing = new BooleanSetting("OwnSwing", true);

    public TimerUtil timer = new TimerUtil();

    public CevPlace() {
        super("CevPlace", Category.COMBAT, "Automatically will place a crystal if the trapped player tries to mine out.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!alwaysActive.getValue()) return;
        if (timer.passedMs((long) delay.getValue())) {
            for (EntityPlayer p : mc.world.playerEntities) {
                if (p == mc.player) continue;
                if (p.getDistance(mc.player) >= range.getValue()) continue;
                if (Yeehaw.INSTANCE.friendManager.isFriend(p.getDisplayNameString())) continue;
                BlockPos playerPos = new BlockPos(p.posX, p.posY, p.posZ);
                BlockPos targetPos = playerPos.up().up();
                if (mc.world.getBlockState(targetPos).getBlock() == Blocks.OBSIDIAN) {
                    if (mc.world.getBlockState(targetPos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(targetPos.up().up()).getBlock().equals(Blocks.AIR)) {
                        BlockUtil.placeBlock(targetPos, true, Items.END_CRYSTAL);
                        timer.reset();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (timer.passedMs((long) delay.getValue())) {
            if (event.getEntityPlayer() == mc.player && !ownSwing.getValue()) return;
            if (event.getEntityPlayer().getDistance(mc.player) >= range.getValue()) return;
            if (Yeehaw.INSTANCE.friendManager.isFriend(event.getEntityPlayer().getDisplayNameString())) return;
            BlockPos targetPos = event.getPos();
            if (mc.world.getBlockState(targetPos).getBlock() == Blocks.OBSIDIAN) {
                if (mc.world.getBlockState(targetPos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(targetPos.up().up()).getBlock().equals(Blocks.AIR)) {
                    BlockUtil.placeBlock(targetPos, true, Items.END_CRYSTAL);
                    timer.reset();
                }
            }
        }
    }
}
