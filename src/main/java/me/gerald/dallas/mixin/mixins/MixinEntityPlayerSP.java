package me.gerald.dallas.mixin.mixins;

import com.mojang.authlib.GameProfile;
import me.gerald.dallas.event.events.MotionEvent;
import me.gerald.dallas.event.events.MotionUpdateEvent;
import me.gerald.dallas.event.events.MotionUpdateEvent.Era;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    @Shadow
    @Final
    public NetHandlerPlayClient connection;
    @Shadow
    protected Minecraft mc;
    @Shadow
    private boolean serverSprintState;
    @Shadow
    private boolean serverSneakState;
    @Shadow
    private int positionUpdateTicks;
    @Shadow
    private float lastReportedPitch;
    @Shadow
    private float lastReportedYaw;
    @Shadow
    private double lastReportedPosZ;
    @Shadow
    private double lastReportedPosY;
    @Shadow
    private double lastReportedPosX;
    @Shadow
    private boolean prevOnGround;
    @Shadow
    private boolean autoJumpEnabled;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Shadow
    protected abstract boolean isCurrentViewEntity();

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void moveHook(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
        MotionEvent event = new MotionEvent(x, y, z);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            super.move(type, event.getX(), event.getY(), event.getZ());
        }
    }

    @Inject(method = "onUpdateWalkingPlayer", at = @At("HEAD"), cancellable = true)
    public void onUpdateWalkingPlayerPreHook(CallbackInfo info) {
        MotionUpdateEvent event = new MotionUpdateEvent(posX, getEntityBoundingBox().minY, posZ, rotationYaw, rotationPitch, onGround);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            info.cancel();
            onUpdateWalkingPlayerSpoof(event);
        }
    }

    /**
     * Spoofs your position and rotation and onGround state sent to the server
     * <p>
     * Derived from EntityPlayerSP#onUpdateWalkingPlayer
     *
     * @param event the pre motion update event
     */
    private void onUpdateWalkingPlayerSpoof(MotionUpdateEvent event) {
        if (!event.getEra().equals(Era.PRE)) {
            return;
        }

        boolean sprinting = isSprinting();
        if (sprinting != serverSprintState) {
            connection.sendPacket(new CPacketEntityAction(this, sprinting ? Action.START_SPRINTING : Action.STOP_SPRINTING));
            serverSprintState = sprinting;
        }

        boolean sneaking = isSneaking();

        if (sneaking != serverSneakState) {
            connection.sendPacket(new CPacketEntityAction(this, sneaking ? Action.START_SNEAKING : Action.STOP_SNEAKING));
            serverSneakState = sneaking;
        }

        if (isCurrentViewEntity()) {
            double x = event.getX();
            double y = event.getY();
            double z = event.getZ();

            float yaw = event.getYaw();
            float pitch = event.getPitch();

            boolean grounded = event.isOnGround();

            double d0 = x - lastReportedPosX;
            double d1 = y - lastReportedPosY;
            double d2 = z - lastReportedPosZ;
            double d3 = yaw - lastReportedYaw;
            double d4 = pitch - lastReportedPitch;

            ++positionUpdateTicks;

            boolean moved = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || positionUpdateTicks >= 20;
            boolean rotated = d3 != 0.0D || d4 != 0.0D;

            if (isRiding()) {
                connection.sendPacket(new CPacketPlayer.PositionRotation(motionX, -999.0, motionZ, yaw, pitch, grounded));
                moved = false;
            } else if (moved && rotated) {
                connection.sendPacket(new CPacketPlayer.PositionRotation(x, y, z, yaw, pitch, grounded));
            } else if (moved) {
                connection.sendPacket(new CPacketPlayer.Position(x, y, z, grounded));
            } else if (rotated) {
                connection.sendPacket(new CPacketPlayer.Rotation(yaw, pitch, grounded));
            } else if (prevOnGround != grounded) {
                connection.sendPacket(new CPacketPlayer(grounded));
            }

            if (moved) {
                lastReportedPosX = x;
                lastReportedPosY = y;
                lastReportedPosZ = z;
                positionUpdateTicks = 0;
            }

            if (rotated) {
                lastReportedYaw = yaw;
                lastReportedPitch = pitch;
            }

            prevOnGround = onGround;
            autoJumpEnabled = mc.gameSettings.autoJump;
        }
    }
}
