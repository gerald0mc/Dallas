package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.ReflectionUtil;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static me.gerald.dallas.utils.ReflectionUtil.REFLECTIONS;

public class PacketLog extends HUDModule {

    @SuppressWarnings("rawtypes") // either too high or java just sucks more than i remember :kotlin:
    public static Map<Class<? extends Packet>, BooleanSetting> packets = new HashMap<>();

    public PacketLog() {
        super(new PacketLogComponent(1, 11, 1, 1), "PacketLog", Category.HUD, "Shows the players ping.");
        REFLECTIONS.getSubTypesOf(Packet.class).forEach(packet ->
                packets.put(packet, register(new BooleanSetting(ReflectionUtil.betterSimpleName(packet), false)))
        );
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        handlePacket(event.getPacket());
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        handlePacket(event.getPacket());
    }

    private void handlePacket(Packet<?> packet) {
        if (!packets.get(packet.getClass()).getValue()) return;

    }
}
