package me.gerald.dallas.features.modules.hud.arraylist;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.world.World;

public class ArrayList extends HUDModule {
    public ModeSetting colorMode = new ModeSetting("ColorMode", "Default", "How the array list is colored.", "Default", "Random", "Category");
    public NumberSetting spacing = new NumberSetting("Spacing", 0, 0, 5, "How far apart the modules are listed.");
    public BooleanSetting skipHUD = new BooleanSetting("SkipHUD", true, "Toggles skipping rendering of HUD Category modules.");

    public ArrayList() {
        super(new ArrayListComponent(1, 181, 1, 1), "ArrayList", Category.HUD, "Renders all enabled modules.");
    }
}
