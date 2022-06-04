package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class StringSetting extends Setting {
    public String value;

    public StringSetting(String name, String value, String description) {
        super(name, description);
        this.value = value;
    }

    public StringSetting(String name, String value, String description, Visibility visibility) {
        super(name, description, visibility);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
