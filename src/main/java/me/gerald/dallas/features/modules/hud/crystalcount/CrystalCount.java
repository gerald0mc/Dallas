package me.gerald.dallas.features.modules.hud.crystalcount;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class CrystalCount extends HUDModule {
    public ModeSetting renderMode = new ModeSetting("RenderMode", "Item", "How the item is rendered.", "Item", "Name");
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true, "Toggles rendering of stack count.");

    public CrystalCount() {
        super(new CrystalCountComponent(1, 91, 1, 1), "CrystalCount", Category.HUD, "Counts your crystals.");
    }
}
