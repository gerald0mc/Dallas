package me.gerald.dallas.features.module.render;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ChorusPredict extends Module {
    public ChorusPredict() {
        super("ChorusPredict", Category.RENDER, "Shows where a player will be teleporting.");
    }

    public NumberSetting timeToRemove = register(new NumberSetting("TimeToRemove", 2, 1, 5));
    public NumberSetting scale = register(new NumberSetting("Scale", 1, 0, 5));

    public HashMap<BlockPos, Long> chorusPos = new HashMap<>();

    @SubscribeEvent
    public void onPacketR(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if(packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                chorusPos.put(new BlockPos(packet.getX(), packet.getY(), packet.getZ()), System.currentTimeMillis());
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        if(chorusPos.isEmpty()) return;
        for(Map.Entry<BlockPos, Long> entry : chorusPos.entrySet()) {
            if(nullCheck()) return; //lol anti crash ig?
            if(System.currentTimeMillis() - entry.getValue() > timeToRemove.getValue() * 1000) {
                chorusPos.remove(entry.getKey());
            }
            double deltaX = MathHelper.clampedLerp(entry.getKey().getX(), entry.getKey().getX(), event.getPartialTicks());
            double deltaY = MathHelper.clampedLerp(entry.getKey().getY(), entry.getKey().getY(), event.getPartialTicks());
            double deltaZ = MathHelper.clampedLerp(entry.getKey().getZ(), entry.getKey().getZ(), event.getPartialTicks());
            Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, 0.25, 0));
            //Render stuff
            GlStateManager.pushMatrix();
            GlStateManager.translate(projection.x, projection.y, 0);
            GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
            mc.fontRenderer.drawStringWithShadow("Player teleported!", -(mc.fontRenderer.getStringWidth("Player teleported!") / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
        }
    }
}
