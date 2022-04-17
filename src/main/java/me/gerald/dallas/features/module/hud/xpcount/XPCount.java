package me.gerald.dallas.features.module.hud.xpcount;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.ModeSetting;

public class XPCount extends HUDModule {
    public XPCount() {
        super(new XPCountComponent(1, 131, 1, 1), "XPCount", Category.HUD, "Counts your xp.");
    }

    public ModeSetting renderMode = register(new ModeSetting("RenderMode", "Item", "Item", "Name"));
}
