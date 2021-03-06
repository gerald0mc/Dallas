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
    public NumberSetting range = new NumberSetting("Range", 4, 1, 6, "Max range of the module.");
    public NumberSetting delay = new NumberSetting("Delay", 100, 0, 2000, "How fast the module will perform actions.");
    public BooleanSetting alwaysActive = new BooleanSetting("AlwaysActive", false, "Toggles if the module is constant or only on block press.");
    public BooleanSetting ownSwing = new BooleanSetting("OwnSwing", true, "Toggles your own swing being a factor.");

    public TimerUtil timer = new TimerUtil();
    public EntityPlayer target = null;

    public CevPlace() {
        super("CevPlace", Category.COMBAT, "Automatically will place a crystal if the trapped player tries to mine out.");
    }

    @Override
    public String getMetaData() {
        return target != null ? target.getDisplayNameString() : "";
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
                        target = p;
                        BlockUtil.placeBlock(targetPos, true, Items.END_CRYSTAL);
                        timer.reset();
                        return;
                    }
                }
            }
            target = null;
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
