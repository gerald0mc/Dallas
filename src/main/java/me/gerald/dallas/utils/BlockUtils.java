package me.gerald.dallas.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockUtils {
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

    public static BlockPos canPlaceCrystal(BlockPos pos) {
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
                return neighbor;
        }
        return null;
    }
}
