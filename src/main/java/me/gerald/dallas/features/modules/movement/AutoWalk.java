package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.utils.BaritoneHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", Category.MOVEMENT, "Example baritone module for gerald");
    }

    public final ModeSetting mode = new ModeSetting("Mode", "Basic", "The way you will be auto walking.", "Basic", "Baritone");

    private boolean overrodeProcess = false;

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
        if (overrodeProcess && BaritoneHelper.isActive())
            BaritoneHelper.stop();
        overrodeProcess = false;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        if (mode.getMode().equalsIgnoreCase("Basic")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        } else {
            if (!overrodeProcess) {
                overrodeProcess = true;
                // go the furthest away from 0, 0
                BaritoneHelper.gotoPosInverted(0, 0);
            }
        }
    }
}
