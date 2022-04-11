package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class NumberSetting extends Setting {
    private float value, min, max;

    public NumberSetting(String name, float value, float min, float max) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public NumberSetting(String name, float value, float min, float max, Visibility visibility) {
        super(name, visibility);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
