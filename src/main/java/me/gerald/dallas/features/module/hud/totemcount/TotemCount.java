package me.gerald.dallas.features.module.hud.totemcount;

import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.module.hud.HUDModule;

public class TotemCount extends HUDModule {
    public TotemCount() {
        super(new TotemCountComponent(1, 61, 1, 1), "XPCount", Category.HUD, "Counts your totems.");
    }
}
