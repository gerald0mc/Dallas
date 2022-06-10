package me.gerald.dallas.features.modules.misc;

import baritone.api.event.events.BlockInteractEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AntiRegear extends Module {
    public NumberSetting range = new NumberSetting("Range", 4, 1, 5, "How far away the client will attempt to mine.");

    public Queue<TileEntityShulkerBox> targetQueue = new ConcurrentLinkedDeque<>();
    public Item[] picList = {Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE};
    public int originalSlot = -1;

    public AntiRegear() {
        super("AntiRegear", Category.MISC, "Breaks any shulker that comes in distance of you.");
    }

    @Override
    public void onDisable() {
        if (originalSlot != -1) originalSlot = -1;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!targetQueue.isEmpty()) {
            for (Item item : picList) {
                int picSlot = InventoryUtil.getItemHotbar(item);
                if (picSlot != -1) {
                    if (mc.player.inventory.currentItem == picSlot) break;
                    originalSlot = mc.player.inventory.currentItem;
                    InventoryUtil.switchToSlot(picSlot);
                    break;
                }
                MessageUtil.sendMessage(ChatFormatting.BOLD + "AntiRegear", "No pickaxe in your hotbar toggling.", MessageUtil.MessageType.ERROR);
                toggle();
                return;
            }
            if (mc.world.getBlockState(targetQueue.peek().getPos()).getBlock().equals(Blocks.AIR) || mc.player.getDistance(targetQueue.peek().getPos().getX(), targetQueue.peek().getPos().getY(), targetQueue.peek().getPos().getZ()) >= range.getValue()) {
                targetQueue.remove();
                return;
            }
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, targetQueue.peek().getPos(), EnumFacing.getDirectionFromEntityLiving(targetQueue.peek().getPos(), mc.player)));
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, targetQueue.peek().getPos(), EnumFacing.getDirectionFromEntityLiving(targetQueue.peek().getPos(), mc.player)));
        } else {
            if (originalSlot != -1) {
                if (mc.player.inventory.currentItem == originalSlot) {
                    originalSlot = -1;
                    return;
                }
                InventoryUtil.switchToSlot(originalSlot);
                originalSlot = -1;
                return;
            }
        }
        for (TileEntity tile : mc.world.loadedTileEntityList) {
            if (tile instanceof TileEntityShulkerBox) {
                if (mc.player.getDistance(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()) >= range.getValue()) continue;
                if (!targetQueue.contains(tile)) {
                    targetQueue.add((TileEntityShulkerBox) tile);
                }
            }
        }
    }
}
