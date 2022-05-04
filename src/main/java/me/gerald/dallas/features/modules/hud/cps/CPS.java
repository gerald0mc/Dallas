package me.gerald.dallas.features.modules.hud.cps;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class CPS extends HUDModule {
    public CPS() {
        super(new CPSComponent(1, 31, 1, 1), "CPS", Category.HUD, "Counts your crystal usage.");
    }
}
