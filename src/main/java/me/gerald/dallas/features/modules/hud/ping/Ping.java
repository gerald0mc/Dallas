package me.gerald.dallas.features.modules.hud.ping;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Ping extends HUDModule {
    public Ping() {
        super(new PingComponent(1, 11, 1, 1), "Ping", Category.HUD, "Shows the players ping.");
    }
}
