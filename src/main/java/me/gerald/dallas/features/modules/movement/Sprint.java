package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Rage", "Rage", "Legit");

    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Sets the player to sprinting.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (nullCheck()) return;
        switch (mode.getMode()) {
            case "Rage":
                while (!mc.player.isSprinting())
                    mc.player.setSprinting(true);
                break;
            case "Legit":
                if (!mc.player.collidedHorizontally && !mc.player.isSneaking() && mc.player.moveForward != 0 || mc.player.moveStrafing != 0) {
                    while (!mc.player.isSprinting()) {
                        mc.player.setSprinting(true);
                    }
                }
                break;
        }
    }
}
