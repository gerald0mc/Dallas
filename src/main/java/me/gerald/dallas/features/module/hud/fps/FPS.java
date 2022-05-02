package me.gerald.dallas.features.module.hud.fps;

import me.gerald.dallas.features.module.hud.HUDModule;

public class FPS extends HUDModule {
    public FPS() {
        super(new FPSComponent(1, 11, 1, 1), "FPS", Category.HUD, "Shows the FPS.");
    }
}
