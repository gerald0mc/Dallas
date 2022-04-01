package me.gerald.dallas.mod.mods;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mod.Module;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.COMBAT, "Stops you from being pushed back.");
    }

    @SubscribeEvent
    public void onPush(PacketEvent.Receive event) {
        if(event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) {
            event.setCanceled(true);
        }
    }
}
