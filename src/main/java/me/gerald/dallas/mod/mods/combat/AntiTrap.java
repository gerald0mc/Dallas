package me.gerald.dallas.mod.mods.combat;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.InventoryUtil;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiTrap extends Module {
    public AntiTrap() {
        super("AntiTrap", Category.COMBAT, "Places a crystal on the block next to you so you cannot be trapped.");
    }

    public BooleanSetting rotate = register(new BooleanSetting("Rotate", true));
    public BooleanSetting fullAnti = register(new BooleanSetting("FullAnti", true));
    public BooleanSetting alwaysActive = register(new BooleanSetting("AlwaysActive", false));
    public NumberSetting distanceToActivate = register(new NumberSetting("DistanceToAct", 10, 0, 30));

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if(!BlockUtil.isSurrounded(playerPos)) return;
        BlockPos targetPos = BlockUtil.canPlaceCrystal(playerPos);
        BlockPos preTrapPos = BlockUtil.isPreTrap(playerPos);
        EntityPlayer player = findClosestPlayer();
        if(player != null && mc.player.getDistance(player) > distanceToActivate.getValue() && !alwaysActive.getValue()) return;
        if(player == null && !alwaysActive.getValue()) return;
        if(targetPos != null && fullAnti.getValue()) {
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
                if(rotate.getValue()) {
                    Yeehaw.INSTANCE.rotationManager.rotateToPosition(targetPos);
                }
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if(mc.player.inventory.currentItem != originalSlot)
                    InventoryUtil.switchToSlot(originalSlot);
            }
        }else if(preTrapPos != null) {
            if(mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(preTrapPos)).isEmpty()) {
                int crystalSlot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
                int originalSlot = mc.player.inventory.currentItem;
                if(crystalSlot != -1 && mc.player.inventory.currentItem != crystalSlot)
                    InventoryUtil.switchToSlot(crystalSlot);
                RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(preTrapPos.getX() + .5, preTrapPos.getY() - .5, preTrapPos.getZ() + .5));
                EnumFacing face;
                if (result == null || result.sideHit == null) {
                    face = EnumFacing.UP;
                } else {
                    face = result.sideHit;
                }
                if(rotate.getValue()) {
                    Yeehaw.INSTANCE.rotationManager.rotateToPosition(preTrapPos);
                }
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(preTrapPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if(mc.player.inventory.currentItem != originalSlot)
                    InventoryUtil.switchToSlot(originalSlot);
            }
        }
    }

    private EntityPlayer findClosestPlayer() {
        if (mc.world.playerEntities.isEmpty())
            return null;
        EntityPlayer closestTarget = null;
        for(EntityPlayer target : mc.world.playerEntities) {
            if(target != mc.player) {
                if(!target.isEntityAlive())
                    continue;
                if(Yeehaw.INSTANCE.friendManager.isFriend(target.getName()))
                    continue;
                if(target.getHealth() <= 0.0f)
                    continue;
                closestTarget = target;
            }
        }
        return closestTarget;
    }
}
