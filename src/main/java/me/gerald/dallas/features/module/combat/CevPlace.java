package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CevPlace extends Module {
    public CevPlace() {
        super("CevPlace", Category.COMBAT, "Automatically will place a crystal if the trapped player tries to mine out.");
    }

    public NumberSetting range = register(new NumberSetting("Range", 4, 1, 6));
    public NumberSetting delay = register(new NumberSetting("Delay", 100, 0, 2000));
    public BooleanSetting alwaysActive = register(new BooleanSetting("AlwaysActive", false));
    public BooleanSetting ownSwing = register(new BooleanSetting("OwnSwing", true));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(!alwaysActive.getValue()) return;
        if(timer.passedMs((long) delay.getValue())) {
            for(EntityPlayer p : mc.world.playerEntities) {
                if(p == mc.player) continue;
                if(p.getDistance(mc.player) >= range.getValue()) continue;
                if(Yeehaw.INSTANCE.friendManager.isFriend(p.getDisplayNameString())) continue;
                BlockPos playerPos = new BlockPos(p.posX, p.posY, p.posZ);
                BlockPos targetPos = playerPos.up().up();
                if(mc.world.getBlockState(targetPos).getBlock() == Blocks.OBSIDIAN) {
                    if (mc.world.getBlockState(targetPos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(targetPos.up().up()).getBlock().equals(Blocks.AIR)) {
                        if(mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(targetPos)).isEmpty()) {
                            int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
                            int originalSlot = mc.player.inventory.currentItem;
                            if(crystalSlot != -1 && mc.player.inventory.currentItem != crystalSlot)
                                InventoryUtil.switchToSlot(crystalSlot);
                            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(targetPos.getX() + .5, targetPos.getY() - .5, targetPos.getZ() + .5));
                            EnumFacing face;
                            if (result == null || result.sideHit == null) {
                                face = EnumFacing.UP;
                            } else {
                                face = result.sideHit;
                            }
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
                            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                            if(mc.player.inventory.currentItem != originalSlot)
                                InventoryUtil.switchToSlot(originalSlot);
                            timer.reset();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.LeftClickBlock event) {
        if(timer.passedMs((long) delay.getValue())) {
            if(event.getEntityPlayer()== mc.player && !ownSwing.getValue()) return;
            if(event.getEntityPlayer().getDistance(mc.player) >= range.getValue()) return;
            if(Yeehaw.INSTANCE.friendManager.isFriend(event.getEntityPlayer().getDisplayNameString())) return;
            BlockPos targetPos = event.getPos();
            if(mc.world.getBlockState(targetPos).getBlock() == Blocks.OBSIDIAN) {
                if (mc.world.getBlockState(targetPos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(targetPos.up().up()).getBlock().equals(Blocks.AIR)) {
                    if(mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(targetPos)).isEmpty()) {
                        int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
                        int originalSlot = mc.player.inventory.currentItem;
                        if(crystalSlot != -1 && mc.player.inventory.currentItem != crystalSlot)
                            InventoryUtil.switchToSlot(crystalSlot);
                        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(targetPos.getX() + .5, targetPos.getY() - .5, targetPos.getZ() + .5));
                        EnumFacing face;
                        if (result == null || result.sideHit == null) {
                            face = EnumFacing.UP;
                        } else {
                            face = result.sideHit;
                        }
                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
                        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                        if(mc.player.inventory.currentItem != originalSlot)
                            InventoryUtil.switchToSlot(originalSlot);
                        timer.reset();
                    }
                }
            }
        }
    }
}
