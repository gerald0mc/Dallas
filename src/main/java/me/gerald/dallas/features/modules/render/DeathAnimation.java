package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;

public class DeathAnimation extends Module {
    public NumberSetting speed = new NumberSetting("Speed", 1, 0.1f, 1);

    public DeathAnimation() {
        super("DeathAnimation", Category.RENDER, "Modifies the speed of the death animation.");
        setBetaModule(true);
    }
}
