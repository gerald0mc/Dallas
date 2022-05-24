package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.event.events.PlayerDamageBlockEvent;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketMine extends Module {

    public PacketMine() {
        super("PacketMine", Category.MISC, "Mining with packets");
    }

    @SubscribeEvent
    public void onDamageBlock(PlayerDamageBlockEvent event) {
        event.setCanceled(true);

        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
    }
}
