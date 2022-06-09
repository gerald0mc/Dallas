package me.gerald.dallas.features.modules.hud.biome;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Biome extends HUDModule {
    public Biome() {
        super(new BiomeComponent(2, 81, 1, 1), "Biome", Category.HUD, "Shows the current biome.");
    }
}
