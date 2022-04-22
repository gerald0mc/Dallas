package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.features.gui.comps.settingcomps.BooleanComponent;
import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.ReflectionUtil;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketLog extends HUDModule {
    public static List<BooleanSetting> settings = new ArrayList<>();
    public static Map<String, Class<Packet<?>>> packets = new HashMap<>();

    public PacketLog() {
        super(new PacketLogComponent(1, 11, 1, 1), "PacketLog", Category.HUD, "Shows the players ping.");
        ReflectionUtil.getSubclasses(Packet.class).forEach(packet -> {
            String full = packet.getName();
            String pkg = packet.getPackage().getName();
            String name = Stringu
            packets.pu
            BooleanSetting setting = new BooleanSetting(, false);
        });
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {

    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {

    }
}
