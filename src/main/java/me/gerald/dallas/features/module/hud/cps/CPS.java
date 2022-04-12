package me.gerald.dallas.features.module.hud.cps;

import me.gerald.dallas.features.module.hud.HUDModule;

public class CPS extends HUDModule {
    public CPS() {
        super(new CPSComponent(1, 11, 1, 1), "CPS", Category.HUD, "Counts your crystal usage.");
    }
}
