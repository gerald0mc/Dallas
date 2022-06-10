package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;

public class LongJump extends Module {
    public NumberSetting speed = new NumberSetting("Speed", 1.2f, 0.1f, 5.0f, "How fast you will be boosted.");

    public LongJump() {
        super("LongJump", Category.MOVEMENT, "Vanilla longjump. (Prob doesn't work)");
    }

    @Override
    public void onEnable() {
        if (mc.player.onGround) mc.player.jump();
        float rotation = clampRotation();
        mc.player.motionX = -Math.sin(rotation) * speed.getValue();
        mc.player.motionZ = Math.cos(rotation) * speed.getValue();
        toggle();
    }

    private float clampRotation() {
        float rotationYaw = mc.player.rotationYaw;
        float n = 1.0f;
        if (mc.player.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        } else if (mc.player.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (mc.player.movementInput.moveStrafe > 0.0f)
            rotationYaw -= 90.0f * n;
        if (mc.player.movementInput.moveStrafe < 0.0f)
            rotationYaw += 90.0f * n;
        return rotationYaw * 0.017453292f;
    }
}
