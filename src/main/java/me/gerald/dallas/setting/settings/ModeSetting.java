package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class ModeSetting extends Setting {
    private final String[] modes;
    private final String defaultMode;
    private int modeIndex;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name);
        this.defaultMode = defaultMode;
        this.modes = modes;
        this.modeIndex = this.indexOf(defaultMode);
    }

    public ModeSetting(String name, String defaultMode, Visibility visibility, String... modes) {
        super(name, visibility);
        this.defaultMode = defaultMode;
        this.modes = modes;
        this.modeIndex = this.indexOf(defaultMode);
    }

    public String getMode() {
        return this.modes[this.modeIndex];
    }

    public void setMode(String mode) {
        int i = this.indexOf(mode);
        this.modeIndex = i == -1 ? 0 : i;
    }

    public String[] getModes() {
        return this.modes;
    }

    public String getDefaultMode() {
        return this.defaultMode;
    }

    public void increase() {
        if (this.modeIndex == this.modes.length - 1) {
            this.modeIndex = 0;
        } else {
            this.modeIndex++;
        }
    }

    public void decrease() {
        if (this.modeIndex == 0) {
            this.modeIndex = this.modes.length - 1;
        } else {
            this.modeIndex--;
        }
    }

    public int indexOf(String name) {
        for (int i = 0; i < this.modes.length; i++) {
            if (this.modes[i].equals(name))
                return i;
        }

        return -1;
    }
}
