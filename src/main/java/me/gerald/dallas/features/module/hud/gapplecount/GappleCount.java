package me.gerald.dallas.features.module.hud.gapplecount;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.ModeSetting;

public class GappleCount extends HUDModule {
    public GappleCount() {
        super(new GappleCountComponent(1, 111, 1, 1), "GappleCount", Category.HUD, "Counts your gapples.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RendeMode", "Item", "Item", "Name"));
}
