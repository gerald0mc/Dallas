package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Sets the player to sprinting.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (nullCheck()) return;
        if (!mc.player.collidedHorizontally && !mc.player.isSneaking() && mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
            mc.player.setSprinting(true);
        }
    }
}
