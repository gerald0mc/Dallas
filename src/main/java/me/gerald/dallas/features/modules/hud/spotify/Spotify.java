package me.gerald.dallas.features.modules.hud.spotify;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Spotify extends HUDModule {
    public Spotify() {
        super(new SpotifyComponent(1, 1, 1, 1), "SpotifyComponent", Category.HUD, "Spotify hud.");
    }
}
