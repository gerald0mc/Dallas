package me.gerald.dallas.features.module.hud.gapplecount;

import me.gerald.dallas.features.module.hud.HUDModule;

public class GappleCount extends HUDModule {
    public GappleCount() {
        super(new GappleCountComponent(1, 81, 1, 1), "GappleCount", Category.HUD, "Counts your gapples.");
    }
}
