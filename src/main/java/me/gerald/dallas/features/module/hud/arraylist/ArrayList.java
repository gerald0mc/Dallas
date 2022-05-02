package me.gerald.dallas.features.module.hud.arraylist;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class ArrayList extends HUDModule {
    public NumberSetting spacing = new NumberSetting("Spacing", 0, 0, 5);
    public BooleanSetting skipHUD = new BooleanSetting("SkipHUD", true);

    public ArrayList() {
        super(new ArrayListComponent(1, 181, 1, 1), "ArrayList", Category.HUD, "Renders all enabled modules.");
    }
}
