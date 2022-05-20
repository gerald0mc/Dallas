package me.gerald.dallas.features.gui.api;

import me.gerald.dallas.managers.module.Module;

public abstract class ModuleContainer extends AbstractContainer {
    public Module module;

    public boolean needsHover = false;

    public ModuleContainer(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
    }
}
