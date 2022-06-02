package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ReverseStep extends Module {
    public ReverseStep() {
        super("ReverseStep", Category.MOVEMENT, "Pulls your nuts down.");
    }

    public NumberSetting power = new NumberSetting("Power", 1.0f, 0.1f, 10.0f);

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if (!mc.player.onGround || mc.player.isOnLadder() || mc.player.isInWater() || mc.player.isInLava() || mc.player.movementInput.jump || mc.player.noClip) return;
        if (mc.player.moveForward == 0.0f && mc.player.moveStrafing == 0.0f) return;
        mc.player.motionY = -(power.getValue());
    }
}
