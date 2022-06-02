package me.gerald.dallas.features.modules.hud.currentconfig;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class CurrentConfig extends HUDModule {
    public CurrentConfig() {
        super(new CurrentConfigComponent(2, 71, 1, 1), "CurrentConfig", Category.HUD, "Renders the players current config.");
    }
}
