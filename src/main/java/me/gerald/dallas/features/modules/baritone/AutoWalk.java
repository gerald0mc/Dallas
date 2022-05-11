package me.gerald.dallas.features.modules.baritone;

import me.gerald.dallas.managers.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoWalk extends Module {
    public AutoWalk() {
        super("AutoWalk", Category.BARITONE, "Walks for you but with baritone!");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
    }
}
