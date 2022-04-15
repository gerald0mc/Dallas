package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public BooleanSetting(String name, boolean value, Visibility visibility) {
        super(name, visibility);
        this.value = value;
    }

    public void cycle() {
        value = !value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
