package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class NumberSetting extends Setting {
    private final float min;
    private final float max;
    private float value;

    public NumberSetting(String name, float value, float min, float max, String description) {
        super(name, description);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public NumberSetting(String name, float value, float min, float max, String description, Visibility visibility) {
        super(name, description, visibility);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }
}
