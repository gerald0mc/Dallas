package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.event.events.MotionEvent;
import me.gerald.dallas.event.events.MotionUpdateEvent;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

/**
 * @author aesthetical
 * @since 5/20/22
 */
public class Speed extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Strafe", "Strafe", "YPort");
    public BooleanSetting strictJump = new BooleanSetting("StrictJump", false);
    public NumberSetting timer = new NumberSetting("Timer", 1.1f, 1.0f, 2.0f);

    private int stage = 4;
    private double motionSpeed, distance;
    private boolean funnyBoolean = false;

    public Speed() {
        super("Speed", Category.MOVEMENT, "zoom zoom lol");
    }

    @Override
    public String getMetaData() {
        return mode.getMode();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        stage = 4;
        motionSpeed = 0.0;
        distance = 0.0;
        funnyBoolean = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (mode.getMode().equalsIgnoreCase("YPort")) {
            if (mc.player.onGround) {
                mc.player.jump();
            } else {
                mc.player.motionY = -1.0;
            }
        }
    }

    @SubscribeEvent
    public void onMotionUpdate(MotionUpdateEvent event) {
        double diffX = mc.player.posX - mc.player.lastTickPosX;
        double diffZ = mc.player.posZ - mc.player.lastTickPosZ;

        distance = Math.sqrt(diffX * diffX + diffZ * diffZ);
    }

    @SubscribeEvent
    public void onMotion(MotionEvent event) {
        if (mode.getMode().equalsIgnoreCase("YPort")) {
            double[] motion = strafe(1.38 * getBaseNCPSpeed() - 0.01);
            event.setX(mc.player.motionX = motion[0]);
            event.setZ(mc.player.motionZ = motion[1]);
        } else if (mode.getMode().equalsIgnoreCase("Strafe")) {
            if (mc.player.isInLava() || mc.player.isInWater()) {
                return;
            }

            if (mc.player.onGround && isMoving()) {
                stage = 2;
            }

            if (stage == 1 && isMoving()) {
                stage = 2;
                motionSpeed = 1.35 * getBaseNCPSpeed() - 0.01;
            } else if (stage == 2) {
                stage = 3;

                if (mc.player.onGround && isMoving()) {
                    double jumpHeight = getJumpHeight();

                    mc.player.motionY = jumpHeight;
                    event.setY(jumpHeight);

                    motionSpeed *= funnyBoolean ? 1.367 : 1.69;
                }
            } else if (stage == 3) {
                stage = 4;
                double adjusted = 0.66 * (distance - getBaseNCPSpeed());
                motionSpeed = distance - adjusted;
            } else if (stage == 4) {
                List<AxisAlignedBB> boxes = mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0));
                if (boxes.size() > 0 || mc.player.collidedVertically) {
                    stage = isMoving() ? 1 : 0;
                }

                motionSpeed = distance - distance / 159.0;
                funnyBoolean = !funnyBoolean;
            }

            motionSpeed = Math.max(motionSpeed, getBaseNCPSpeed());

            double[] motion = strafe(motionSpeed);
            if (isMoving()) {
                event.setX(motion[0]);
                event.setZ(motion[1]);
            } else {
                event.setX(0.0);
                event.setZ(0.0);
            }
        }

        if (isMoving() && timer.getValue() > 1.0f) {
            // TODO: timer accessor
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {

        // if NCP sets us back / we get teleported
        if (event.getPacket() instanceof SPacketPlayerPosLook) {

            // reset values
            stage = 4;
            motionSpeed = 0.0;
            distance = 0.0;
            funnyBoolean = true;
        }
    }

    private double getJumpHeight() {
        double jumpHeight = 0.3995;
        if (strictJump.getValue()) {
            jumpHeight = 0.41999998688697815;
        }

        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
            jumpHeight += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1;
        }

        return jumpHeight;
    }

    private double getBaseNCPSpeed() {
        double baseSpeed = 0.2873;

        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            baseSpeed *= 1.0 + 0.2 * (mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    private boolean isMoving() {
        return mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f;
    }

    private double[] strafe(double speed) {
        float[] movements = getMovement();

        float forward = movements[0];
        float strafe = movements[1];

        double sin = -Math.sin(Math.toRadians(movements[2]));
        double cos = Math.cos(Math.toRadians(movements[2]));

        return new double[]{forward * speed * sin + strafe * speed * cos,
                forward * speed * cos - strafe * speed * sin};
    }

    private float[] getMovement() {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += forward > 0.0f ? -45.0f : 45.0f;
            } else if (strafe < 0.0f) {
                yaw += forward > 0.0f ? 45.0f : -45.0f;
            }

            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        return new float[] { forward, strafe, yaw };
    }
}
