package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.features.module.hud.HUDModule;

public class PacketLog extends HUDModule {
    public PacketLog() {
        super(new PacketLogComponent(1, 11, 1, 1), "PacketLog", Category.HUD, "Shows the players ping.");
    }
}
