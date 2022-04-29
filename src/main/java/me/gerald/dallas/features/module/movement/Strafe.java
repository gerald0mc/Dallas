package me.gerald.dallas.features.module.movement;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.mixin.mixins.MixinEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Strafe extends Module {
    public Strafe() {
        super("Strafe", Category.MOVEMENT, "Automatically strafes for the player.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || ((MixinEntity) mc.player).getIsInWeb()) return;
        if(mc.player.onGround) {
            mc.player.jump();
        }else {
            mc.player.motionY = -0.11f;
        }
    }
}
