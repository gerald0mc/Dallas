package me.gerald.dallas.features.module.hud.ping;

import me.gerald.dallas.features.module.hud.HUDModule;

public class Ping extends HUDModule {
    public Ping() {
        super(new PingComponent(1, 11, 1, 1), "Ping", Category.HUD, "Shows the players ping.");
    }
}
