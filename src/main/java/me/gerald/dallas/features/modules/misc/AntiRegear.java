package me.gerald.dallas.features.modules.misc;

import baritone.api.event.events.BlockInteractEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AntiRegear extends Module {
    public NumberSetting range = new NumberSetting("Range", 4, 1, 5, "How far away the client will attempt to mine.");
    public ColorSetting boxColor = new ColorSetting("BoxColor", 255, 0, 0, 125, "Box color.");

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
            if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe)) {
                for (Item item : picList) {
                    int picSlot = InventoryUtil.getItemHotbar(item);
                    if (picSlot != -1) {
                        if (mc.player.inventory.currentItem == picSlot) break;
                        originalSlot = mc.player.inventory.currentItem;
                        InventoryUtil.switchToSlot(picSlot);
                        return;
                    }
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "AntiRegear", "No pickaxe in your hotbar toggling.", MessageUtil.MessageType.ERROR);
                    toggle();
                    return;
                }
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

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (targetQueue.isEmpty()) return;
        AxisAlignedBB box = mc.world.getBlockState(targetQueue.peek().getPos()).getSelectedBoundingBox(mc.world, targetQueue.peek().getPos()).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        RenderUtil.prepare();
        RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor.getR() / 255f, boxColor.getG() / 255f, boxColor.getB() / 255f, boxColor.getA() / 255f);
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor.getR() / 255f, boxColor.getG() / 255f, boxColor.getB() / 255f, 1f);
        RenderUtil.release();
    }
}
