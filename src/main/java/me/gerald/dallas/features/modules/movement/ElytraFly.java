package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", Category.MOVEMENT, "Basic AF ElytraFly. (Confirmed working on 5b5t)");
    }

    public NumberSetting boost = new NumberSetting("Boost", 1.2f, 0.1f, 5.0f, "How fast you will be boosted.");
    public BooleanSetting hover = new BooleanSetting("Hover", true, "Toggles not falling while in the air.");
    public BooleanSetting autoLiftoff = new BooleanSetting("AutoLiftoff", true, "Toggles auto liftoff after a certain of falling.");
    public NumberSetting fallDistance = new NumberSetting("FallDistance", 2, 1, 5, "How far you must fall to toggle AutoLiftoff.");
    public BooleanSetting verticalControls = new BooleanSetting("VerticalControls", true, "Toggling you being able to move up and down easily.");

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(!mc.player.isElytraFlying()) {
            if(autoLiftoff.getValue() && mc.player.fallDistance > fallDistance.getValue()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            return;
        }
        if(hover.getValue() && (mc.player.movementInput.moveForward == 0 && mc.player.movementInput.moveStrafe == 0)) {
            mc.player.motionY = -1.0E-4;
            return;
        }
        if(verticalControls.getValue()) {
            if(mc.gameSettings.keyBindJump.isPressed())
                mc.player.motionY = boost.getValue() / 10;
            else if (mc.gameSettings.keyBindSneak.isPressed())
                mc.player.motionY = -(boost.getValue() / 10);
        }
        float rotation = clampRotation();
        mc.player.motionX = -Math.sin(rotation) * boost.getValue();
        mc.player.motionZ = Math.cos(rotation) * boost.getValue();
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
