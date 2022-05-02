package me.gerald.dallas.features.module.hud.coordinates;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Coordinates extends HUDModule {
    public BooleanSetting xyz = new BooleanSetting("XYZ", true);
    public BooleanSetting nether = new BooleanSetting("Nether", true);
    public BooleanSetting spoof = new BooleanSetting("Spoof", false);
    public NumberSetting spoofAmount = new NumberSetting("SpoofAmount(Value*1000)", 5, 1, 20, () -> spoof.getValue());

    public Coordinates() {
        super(new CoordinatesComponent(1, mc.displayHeight, 1, 1), "Coordinates", Category.HUD, "Shows the player coordinates.");
    }
}
