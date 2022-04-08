package me.gerald.dallas.mod.mods.hud.watermark;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.mod.HUDModule;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Watermark extends HUDModule {
    public Watermark() {
        super(new WatermarkComponent(1, 1, 1, 1), "Watermark", Category.HUD, "Watermark component.");
    }
}
