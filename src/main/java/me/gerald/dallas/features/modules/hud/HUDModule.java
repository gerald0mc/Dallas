package me.gerald.dallas.features.modules.hud;

import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.managers.module.Module;

public abstract class HUDModule extends Module {
    private final HUDContainer container;

    public HUDModule(HUDContainer container, String name, Category category, String description) {
        super(name, category, description);
        this.container = container;
    }

    public HUDContainer getContainer() {
        return container;
    }
}
