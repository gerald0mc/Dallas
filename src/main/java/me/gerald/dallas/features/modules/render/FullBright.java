package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright extends Module {
    public float defaultGamma = -1f;

    public FullBright() {
        super("FullBright", Category.RENDER, "Basic AF FullBright.");
    }

    @Override
    public void onEnable() {
        defaultGamma = mc.gameSettings.gammaSetting;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        mc.gameSettings.gammaSetting = 100f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = defaultGamma;
    }
}
