package me.gerald.dallas.features.modules.hud.speed;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Speed extends HUDModule {
    public Speed() {
        super(new SpeedComponent(1, 181, 1, 1), "Speed", Category.HUD, "Renders all enabled modules.");
    }
}
