package me.gerald.dallas.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PlayerDamageBlockEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class Ruler extends Module {
    public ColorSetting boxColor = new ColorSetting("BoxColor", 255, 255, 255, 125, "Ruler box color.");

    public BlockPos rulerBlock = null;

    public Ruler() {
        super("Ruler", Category.RENDER, "Tells you how far places are from each other.");
    }

    @SubscribeEvent
    public void onBlockDamage(PlayerDamageBlockEvent event) {
        event.setCanceled(true);
        if(rulerBlock == null) {
            rulerBlock = event.getPos();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Ruler", "Added position to pos list.", true);
        } else {
            int blocksBetween = (int) rulerBlock.getDistance(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Ruler", "Distance between the two spots is " + ChatFormatting.AQUA + blocksBetween + ChatFormatting.RESET + ".", true);
            rulerBlock = null;
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(nullCheck()) return;
        if(rulerBlock == null) return;
        AxisAlignedBB box = mc.world.getBlockState(rulerBlock).getSelectedBoundingBox(mc.world, rulerBlock).offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);
        RenderUtil.prepare();
        RenderGlobal.renderFilledBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ,boxColor.getR() / 255f, boxColor.getG() / 255f, boxColor.getB() / 255f, boxColor.getA() / 255f);
        RenderGlobal.drawBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor.getR() / 255f, boxColor.getG() / 255f, boxColor.getB() / 255f, 1f);
        RenderUtil.release();
    }
}
