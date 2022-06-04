package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.event.events.PlayerDamageBlockEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.mixins.IRenderGlobal;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class PacketMine extends Module {
    public BooleanSetting antiNeededlocks = new BooleanSetting("AntiNeededBlocks", true, "Stops you from mining certain important blocks.");

    public BlockPos currentBlock = null;

    public PacketMine() {
        super("PacketMine", Category.MISC, "Mining with packets");
    }

    @SubscribeEvent
    public void onDamageBlock(PlayerDamageBlockEvent event) {
        event.setCanceled(true);
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        currentBlock = event.getPos();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(currentBlock == null) return;
        if(mc.world.getBlockState(currentBlock).getBlock().equals(Blocks.AIR)) currentBlock = null;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        if(currentBlock != null) {
            AxisAlignedBB box = mc.world.getBlockState(currentBlock).getSelectedBoundingBox(mc.world, currentBlock).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
            RenderUtil.prepare();
            GL11.glLineWidth(1f);
            RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1, 0, 0, 125 / 255f);
            RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, 1, 0, 0, 1);
            RenderUtil.release();
        }
    }
}
