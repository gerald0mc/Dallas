package me.gerald.dallas.features.modules.hud.server;

import me.gerald.dallas.features.modules.hud.HUDModule;

public class Server extends HUDModule {
    public Server() {
        super(new ServerComponent(1, 21, 1, 1), "Server", Category.HUD, "Displays the current server you are ond.");
    }
}
