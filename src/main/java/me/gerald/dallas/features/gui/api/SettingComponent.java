package me.gerald.dallas.features.gui.api;

import me.gerald.dallas.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public abstract class SettingComponent extends AbstractContainer {
    public Setting setting;

    public boolean last = false;
    public boolean needsHover = false;

    public SettingComponent(Setting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
    }

    public boolean isVisible() {
        return setting.isVisible();
    }
}
