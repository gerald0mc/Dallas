package me.gerald.dallas.features.module.hud.crystalcount;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class CrystalCount extends HUDModule {
    public ModeSetting renderMode = new ModeSetting("RenderMode", "Item", "Item", "Name");
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true);

    public CrystalCount() {
        super(new CrystalCountComponent(1, 91, 1, 1), "CrystalCount", Category.HUD, "Counts your crystals.");
    }
}
