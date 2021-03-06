package me.gerald.dallas.features.modules.hud.totemcount;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.ModeSetting;

public class TotemCount extends HUDModule {
    public ModeSetting renderMode = new ModeSetting("RenderMode", "Item", "How the item is rendered.", "Item", "Name");

    public TotemCount() {
        super(new TotemCountComponent(1, 71, 1, 1), "TotemCount", Category.HUD, "Counts your totems.");
    }
}
