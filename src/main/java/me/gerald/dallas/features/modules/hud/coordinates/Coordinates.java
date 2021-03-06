package me.gerald.dallas.features.modules.hud.coordinates;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Coordinates extends HUDModule {
    public BooleanSetting xyz = new BooleanSetting("XYZ", true, "Toggles rendering of XYZ strings.");
    public BooleanSetting nether = new BooleanSetting("Nether", true, "Toggles rendering of nether coordinates.");
    public BooleanSetting spoof = new BooleanSetting("Spoof", false, "Toggles spoofing of coordinates.");
    public NumberSetting spoofAmount = new NumberSetting("SpoofAmount(Value*1000)", 5, 1, 20, "How much the coordinates will be spoofed.", () -> spoof.getValue());

    public Coordinates() {
        super(new CoordinatesComponent(1, mc.displayHeight, 1, 1), "Coordinates", Category.HUD, "Shows the player coordinates.");
    }
}
