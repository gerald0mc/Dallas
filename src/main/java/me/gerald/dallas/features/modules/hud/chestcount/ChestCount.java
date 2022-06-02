package me.gerald.dallas.features.modules.hud.chestcount;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class ChestCount extends HUDModule {
    public ChestCount() {
        super(new ChestCountComponent(2, 61, 1, 1), "ChestCount", Category.HUD, "Counts chests for you.");
    }
}
