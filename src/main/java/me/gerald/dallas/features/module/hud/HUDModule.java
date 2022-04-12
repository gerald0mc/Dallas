package me.gerald.dallas.features.module.hud;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.gui.api.HUDContainer;

public class HUDModule extends Module {
    private final HUDContainer container;

    public HUDModule(HUDContainer container, String name, Category category, String description) {
        super(name, category, description);
        this.container = container;
    }

    public HUDContainer getContainer() {
        return container;
    }
}