package me.gerald.dallas.mod.mods.hud.crystalcount;

import me.gerald.dallas.mod.HUDModule;

public class CrystalCount extends HUDModule {
    public CrystalCount() {
        super(new CrystalCountComponent(1, 11, 1, 1), "CrystalCount", Category.HUD, "Counts your crystal usage.");
    }
}
