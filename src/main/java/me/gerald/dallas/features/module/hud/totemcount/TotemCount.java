package me.gerald.dallas.features.module.hud.totemcount;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.ModeSetting;

public class TotemCount extends HUDModule {
    public ModeSetting renderMode = register(new ModeSetting("RenderMode", "Item", "Item", "Name"));

    public TotemCount() {
        super(new TotemCountComponent(1, 71, 1, 1), "TotemCount", Category.HUD, "Counts your totems.");
    }
}
