package me.gerald.dallas.managers;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mixin.mixins.ICPacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RotationManager {
    public RotationManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean rotate = false;
    private double yaw = 0;
    private double pitch = 0;

    private void lookAtPlayer(double px, double py, double pz, EntityPlayer me) {
        double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    public double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }

    private void packetFlow() {
        Minecraft.getMinecraft().player.rotationPitch += 0.0004;
    }

    public void setRotations(BlockPos pos) {
        yaw = calculateLookAt(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().player)[0];
        pitch = calculateLookAt(pos.getX(), pos.getY(), pos.getZ(), Minecraft.getMinecraft().player)[1];
    }

    public void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        startRotate();
    }

    private void resetRotations() {
        yaw = Minecraft.getMinecraft().player.rotationYaw;
        pitch = Minecraft.getMinecraft().player.rotationPitch;
    }

    @SubscribeEvent
    public void onUpdate(PacketEvent.Send event) {
        if (rotate) {
            if (event.getPacket() instanceof CPacketPlayer) {
                ((ICPacketPlayer) event.getPacket()).setPitch((float) pitch);
                ((ICPacketPlayer) event.getPacket()).setYaw((float) yaw);
                endRotate();
            }
        }
    }

    public void startRotate() {
        packetFlow();
        rotate = true;
        packetFlow();
    }

    public void endRotate() {
        resetRotations();
        packetFlow();
        rotate = false;
    }
}
