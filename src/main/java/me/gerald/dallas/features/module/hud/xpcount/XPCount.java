package me.gerald.dallas.features.module.hud.xpcount;

import me.gerald.dallas.features.module.hud.HUDModule;

public class XPCount extends HUDModule {
    public XPCount() {
        super(new XPCountComponent(1, 91, 1, 1), "XPCount", Category.HUD, "Counts your xp.");
    }
}
