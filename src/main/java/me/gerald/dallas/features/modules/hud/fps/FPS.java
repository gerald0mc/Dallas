package me.gerald.dallas.features.modules.hud.fps;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class FPS extends HUDModule {
    public FPS() {
        super(new FPSComponent(1, 11, 1, 1), "FPS", Category.HUD, "Shows the FPS.");
    }
}
