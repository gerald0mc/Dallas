package me.gerald.dallas.managers;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mixin.mixins.ICPacketPlayerMixin;
import me.gerald.dallas.utils.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager {
    public boolean shouldRotate = false;
    public float desiredPitch;
    public float desiredYaw;

    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (!shouldRotate)
            return;
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            final ICPacketPlayerMixin accessor = (ICPacketPlayerMixin) packet;
            accessor.setYawAccessor(desiredYaw);
            accessor.setPitchAccessor(desiredPitch);
            shouldRotate = false;
        }
    }

    public void rotateToEntity(Entity entity) {
        final float[] angle = MathUtil.calcAngle(Minecraft.getMinecraft().player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()), entity.getPositionVector());
        shouldRotate = true;
        desiredYaw = angle[0];
        desiredPitch = angle[1];
    }

    public void rotateToPosition(BlockPos position) {
        final float[] angle = MathUtil.calcAngle(Minecraft.getMinecraft().player.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()), new Vec3d(position.getX() + 0.5f, position.getY() - 0.5f, position.getZ()));
        shouldRotate = true;
        desiredYaw = angle[0];
        desiredPitch = angle[1];
    }
}
