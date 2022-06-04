package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class BooleanSetting extends Setting {
    private boolean value;

    public BooleanSetting(String name, boolean value, String description) {
        super(name, description);
        this.value = value;
    }

    public BooleanSetting(String name, boolean value, String description, Visibility visibility) {
        super(name, description, visibility);
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
