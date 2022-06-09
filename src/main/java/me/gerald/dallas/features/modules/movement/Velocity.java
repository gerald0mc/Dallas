package me.gerald.dallas.features.modules.movement;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.MOVEMENT, "Cancels velocity packets.");
    }

    @SubscribeEvent
    public void onPacketR(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion)
            event.setCanceled(true);
    }
}
