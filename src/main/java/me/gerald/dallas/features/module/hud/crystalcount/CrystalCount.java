package me.gerald.dallas.features.module.hud.crystalcount;

import me.gerald.dallas.features.module.hud.HUDModule;

public class CrystalCount extends HUDModule {
    public CrystalCount() {
        super(new CrystalCountComponent(1, 71, 1, 1), "CrystalCount", Category.HUD, "Counts your crystals.");
    }
}
