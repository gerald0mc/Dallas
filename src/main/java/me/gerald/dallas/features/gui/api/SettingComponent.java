package me.gerald.dallas.features.gui.api;

import me.gerald.dallas.setting.Setting;
import net.minecraft.client.Minecraft;

public abstract class SettingComponent extends AbstractContainer {
    public Setting setting;

    public boolean last = false;

    public SettingComponent(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public boolean isVisible() {
        return setting.isVisible();
    }
}
