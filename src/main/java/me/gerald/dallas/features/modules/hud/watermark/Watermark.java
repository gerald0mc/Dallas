package me.gerald.dallas.features.modules.hud.watermark;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Watermark extends HUDModule {
    public Watermark() {
        super(new WatermarkComponent(1, 1, 1, 1), "Watermark", Category.HUD, "Watermark component.");
    }
}
