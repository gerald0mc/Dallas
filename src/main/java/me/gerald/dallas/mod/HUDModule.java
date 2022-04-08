package me.gerald.dallas.mod;

import me.gerald.dallas.gui.api.HUDContainer;

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
