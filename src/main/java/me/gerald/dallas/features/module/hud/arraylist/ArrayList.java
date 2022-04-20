package me.gerald.dallas.features.module.hud.arraylist;

import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class ArrayList extends HUDModule {
    public ArrayList() {
        super(new ArrayListComponent(1, 181, 1, 1), "ArrayList", Category.HUD, "Renders all enabled modules.");
    }

    public NumberSetting spacing = register(new NumberSetting("Spacing", 0, 0, 5));
    public BooleanSetting skipHUD = register(new BooleanSetting("SkipHUD", true));
}
