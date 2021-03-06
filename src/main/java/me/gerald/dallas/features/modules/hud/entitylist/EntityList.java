package me.gerald.dallas.features.modules.hud.entitylist;

import me.gerald.dallas.features.modules.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;

public class EntityList extends HUDModule {
    public BooleanSetting playersOnly = new BooleanSetting("PlayersOnly", false, "Toggles only rendering players.");

    public EntityList() {
        super(new EntityListComponent(1, 1, 1, 1), "EntityList", Category.HUD, "Lists all entities near you.");
    }
}
