package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.InventoryUtil;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Surround extends Module {
    public Surround() {
        super("Surround", Category.COMBAT, "Surrounds you in obsidian.");
    }

    BlockPos playerPos;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if(BlockUtil.isSurrounded(playerPos) || mc.player.isAirBorne || mc.player.isOnLadder() || mc.player.isInLava() || mc.player.isInWater()) return;
        for(BlockPos pos : BlockUtil.getSurroundBlocks(playerPos)) {
            if(mc.world.getBlockState(pos).getBlock() == Blocks.AIR) {
                int obsidianSlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
                int originalSlot = mc.player.inventory.currentItem;
                if(obsidianSlot != -1 && mc.player.inventory.currentItem != obsidianSlot)
                    InventoryUtil.switchToSlot(obsidianSlot);
                RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + .5, pos.getY() - .5, pos.getZ() + .5));
                EnumFacing face;
                if (result == null || result.sideHit == null) {
                    face = EnumFacing.UP;
                } else {
                    face = result.sideHit;
                }
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face, EnumHand.MAIN_HAND, 0, 0, 0));
                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if(mc.player.inventory.currentItem != originalSlot)
                    InventoryUtil.switchToSlot(originalSlot);
            }
        }
    }
}
