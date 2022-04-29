package me.gerald.dallas.features.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoBurrow extends Module {
    public NumberSetting distanceToAct = new NumberSetting("DistanceToAct", 7, 1, 10);
    public NumberSetting offset = new NumberSetting("Offset", 7, -20, 20);
    public int oldSlot = -1;

    public AutoBurrow() {
        super("AutoBurrow", Category.COMBAT, "Automatically burrows the player.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        BlockPos playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        EntityPlayer entity = BlockUtil.findClosestPlayer();
        if (entity == null) return;
        if (entity.getDistance(mc.player) >= distanceToAct.getValue()) return;
        if (mc.world.getBlockState(playerPos).getBlock() == Blocks.OBSIDIAN) return;
        if (mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || !mc.player.onGround) return;
        if (intersectsWithEntity(playerPos)) return;
        int obbySlot = InventoryUtil.getItemHotbar(Item.getItemFromBlock(Blocks.OBSIDIAN));
        if (obbySlot != -1) {
            oldSlot = mc.player.inventory.currentItem;
            InventoryUtil.switchToSlot(obbySlot);
        } else {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Auto Burrow", "You have no Obsidian to burrow with toggling!", true);
            toggle();
            return;
        }
        doBurrow(playerPos);
    }

    public void doBurrow(BlockPos targetPos) {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(targetPos.getX() + .5, targetPos.getY() - .5, targetPos.getZ() + .5));
        EnumFacing face;
        if (result == null || result.sideHit == null) {
            face = EnumFacing.UP;
        } else {
            face = result.sideHit;
        }
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPos, face, EnumHand.MAIN_HAND, 0, 0, 0));
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));
        InventoryUtil.switchToSlot(oldSlot);
        oldSlot = -1;
    }

    public boolean intersectsWithEntity(BlockPos pos) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}
