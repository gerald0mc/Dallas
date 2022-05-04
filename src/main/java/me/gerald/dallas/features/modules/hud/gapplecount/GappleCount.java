package me.gerald.dallas.features.modules.hud.gapplecount;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class GappleCount extends HUDModule {
    public ModeSetting renderMode = new ModeSetting("RendeMode", "Item", "Item", "Name");
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true);

    public GappleCount() {
        super(new GappleCountComponent(1, 111, 1, 1), "GappleCount", Category.HUD, "Counts your gapples.");
    }
}
