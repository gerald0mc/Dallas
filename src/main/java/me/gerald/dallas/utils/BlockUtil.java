package me.gerald.dallas.utils;

import me.gerald.dallas.Yeehaw;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

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
}
