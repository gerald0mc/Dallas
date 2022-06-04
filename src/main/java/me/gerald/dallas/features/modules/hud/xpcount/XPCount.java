package me.gerald.dallas.features.modules.hud.xpcount;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class XPCount extends HUDModule {
    public ModeSetting renderMode = new ModeSetting("RenderMode", "Item", "How the item is rendered.", "Item", "Name");
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true, "Toggles the rendering of the items stack count.");

    public XPCount() {
        super(new XPCountComponent(1, 131, 1, 1), "XPCount", Category.HUD, "Counts your xp.");
    }
}
