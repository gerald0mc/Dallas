package me.gerald.dallas.setting.settings;

import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.Visibility;

public class ModeSetting extends Setting {
    private final String[] modes;
    private final String defaultMode;
    private int modeIndex;

    public ModeSetting(String name, String defaultMode, String description, String... modes) {
        super(name, description);
        this.defaultMode = defaultMode;
        this.modes = modes;
        modeIndex = indexOf(defaultMode);
    }

    public ModeSetting(String name, String defaultMode, String description, Visibility visibility, String... modes) {
        super(name, description, visibility);
        this.defaultMode = defaultMode;
        this.modes = modes;
        modeIndex = indexOf(defaultMode);
    }

    public String getMode() {
        return modes[modeIndex];
    }

    public void setMode(String mode) {
        int i = indexOf(mode);
        modeIndex = i == -1 ? 0 : i;
    }

    public String[] getModes() {
        return modes;
    }

    public String getDefaultMode() {
        return defaultMode;
    }

    public void increase() {
        if (modeIndex == modes.length - 1) {
            modeIndex = 0;
        } else {
            modeIndex++;
        }
    }

    public void decrease() {
        if (modeIndex == 0) {
            modeIndex = modes.length - 1;
        } else {
            modeIndex--;
        }
    }

    public int indexOf(String name) {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(name))
                return i;
        }

        return -1;
    }
}
