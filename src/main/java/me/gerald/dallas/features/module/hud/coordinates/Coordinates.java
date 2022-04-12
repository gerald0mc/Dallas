package me.gerald.dallas.features.module.hud.coordinates;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;

public class Coordinates extends HUDModule {
    public Coordinates() {
        super(new CoordinatesComponent(1, mc.displayHeight, 1, 1), "Coordinates", Category.HUD, "Shows the player coordinates.");
    }

    public BooleanSetting nether = register(new BooleanSetting("Nether", true));
}
