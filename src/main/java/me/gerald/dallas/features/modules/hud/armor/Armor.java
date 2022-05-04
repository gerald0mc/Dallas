package me.gerald.dallas.features.modules.hud.armor;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;

public class Armor extends HUDModule {
    public ModeSetting alignment = new ModeSetting("Alignment", "Sideways", "Sideways", "Vertical");
    public BooleanSetting reverse = new BooleanSetting("Reverse", false);
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true);

    public Armor() {
        super(new ArmorComponent(mc.displayWidth / 2, mc.displayHeight - 25, 1, 1), "Armor", Category.HUD, "Shows the armor you are wearing.");
    }
}
