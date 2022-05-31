package me.gerald.dallas.features.modules.hud.speed;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Speed extends HUDModule {
    public Speed() {
        super(new SpeedComponent(1, 51, 1, 1), "SpeedView", Category.HUD, "Renders the players speed.");
    }
}
