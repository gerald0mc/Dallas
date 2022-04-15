package me.gerald.dallas.utils;

import me.gerald.dallas.Yeehaw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class BlockUtil {
    public static boolean isSurrounded(BlockPos playerPos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos neighbor = playerPos.offset(facing);
            Block block = Minecraft.getMinecraft().world.getBlockState(neighbor).getBlock();
            if (block.equals(Blocks.OBSIDIAN) || block.equals(Blocks.BEDROCK))
                continue;
            return false;
        }
        return true;
    }

    public static BlockPos canPlaceCrystalSurround(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos neighbor = pos.offset(facing);
            if (Minecraft.getMinecraft().world.getBlockState(neighbor.up()).getBlock().equals(Blocks.AIR) && Minecraft.getMinecraft().world.getBlockState(neighbor.up().up()).getBlock().equals(Blocks.AIR))
                return neighbor;
        }
        return null;
    }

    public static BlockPos isPreTrap(BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos neighbor = pos.offset(facing);
            if (Minecraft.getMinecraft().world.getBlockState(neighbor.up()).getBlock().equals(Blocks.OBSIDIAN) && Minecraft.getMinecraft().world.getBlockState(neighbor.up().up()).getBlock().equals(Blocks.AIR) && Minecraft.getMinecraft().world.getBlockState(neighbor.up().up().up()).getBlock().equals(Blocks.AIR))
                return neighbor.up();
        }
        return null;
    }

    public static BlockPos[] getSurroundBlocks(BlockPos pos) {
        BlockPos[] blocks = new BlockPos[4];
        int i = 0;
        for(EnumFacing facing : EnumFacing.values()) {
            if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) continue;
            BlockPos neighbor = pos.offset(facing);
            blocks[i] = neighbor;
            i += 1;
        }
        return blocks;
    }

    public static EntityPlayer findClosestPlayer() {
        if (Minecraft.getMinecraft().world.playerEntities.isEmpty())
            return null;
        EntityPlayer closestTarget = null;
        for(EntityPlayer target : Minecraft.getMinecraft().world.playerEntities) {
            if(target != Minecraft.getMinecraft().player) {
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

    public static void placeBlock(BlockPos targetPos, boolean switchItem, Item itemToSwitch) {
        if(Minecraft.getMinecraft().world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(targetPos)).isEmpty()) {
            int slot = InventoryUtil.getItemHotbar(itemToSwitch);
            int originalSlot = Minecraft.getMinecraft().player.inventory.currentItem;
            if(slot != -1 && Minecraft.getMinecraft().player.inventory.currentItem != slot && switchItem)
                InventoryUtil.switchToSlot(slot);
            RayTraceResult result = Minecraft.getMinecraft().world.rayTraceBlocks(new Vec3d(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight(), Minecraft.getMinecraft().player.posZ), new Vec3d(targetPos.getX() + .5, targetPos.getY() - .5, targetPos.getZ() + .5));
            EnumFacing face;
            if (result == null || result.sideHit == null)
                face = EnumFacing.UP;
            else
                face = result.sideHit;
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            if(Minecraft.getMinecraft().player.inventory.currentItem != originalSlot && switchItem)
                InventoryUtil.switchToSlot(originalSlot);
        }
    }
}
