package me.gerald.dallas.features.modules.hud.armor;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;

public class Armor extends HUDModule {
    public ModeSetting alignment = new ModeSetting("Alignment", "Sideways", "Which way the armor will render.", "Sideways", "Vertical");
    public NumberSetting offset = new NumberSetting("Offset", 20, 20, 100, "How far apart the armor will render.");
    public BooleanSetting reverse = new BooleanSetting("Reverse", false, "Toggles the armor rendering in reverse.");
    public BooleanSetting durability = new BooleanSetting("Durability", true, "Toggles the armor rendering its durability.");
    public BooleanSetting stackCount = new BooleanSetting("StackCount", true, "Toggles the stack count rendering.");

    public Armor() {
        super(new ArmorComponent(mc.displayWidth / 2, mc.displayHeight - 25, 1, 1), "Armor", Category.HUD, "Shows the armor you are wearing.");
    }
}
