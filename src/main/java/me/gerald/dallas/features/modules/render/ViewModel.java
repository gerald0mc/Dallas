package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.mixins.MixinItemRenderer;
import me.gerald.dallas.setting.settings.BooleanSetting;

/**
 * @author bush
 * @since 5/6/2022
 * @see MixinItemRenderer
 */
public class ViewModel extends Module {
    public static ViewModel INSTANCE;

    public BooleanSetting noSway = new BooleanSetting("No Sway", false);

    public ViewModel() {
        super("ViewModel", Category.RENDER, "Alters the appearance of held items");
        INSTANCE = this;
    }
}
