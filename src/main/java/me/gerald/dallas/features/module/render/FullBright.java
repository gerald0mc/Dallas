package me.gerald.dallas.features.module.render;

import me.gerald.dallas.features.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Category.RENDER, "LET THERE BE LIGHT!");
    }

    public float originalGamma = 0f;

    @Override
    public void onEnable() {
        originalGamma = mc.gameSettings.gammaSetting;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        mc.gameSettings.gammaSetting = 100f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = originalGamma;
        originalGamma = 0f;
    }
}
