package me.gerald.dallas.gui.api;

import me.gerald.dallas.setting.Setting;

public abstract class SettingComponent extends AbstractContainer {
    public Setting setting;

    public SettingComponent(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public boolean isVisible() {
        return setting.isVisible();
    }
}
