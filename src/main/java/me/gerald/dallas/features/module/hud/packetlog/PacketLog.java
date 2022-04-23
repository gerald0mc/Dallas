package me.gerald.dallas.features.module.hud.packetlog;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.features.gui.comps.settingcomps.BooleanComponent;
import me.gerald.dallas.features.module.hud.HUDModule;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ReflectionUtil;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes") // Doesn't happen in kotlin ;)
public class PacketLog extends HUDModule {
    public static List<BooleanComponent> cPacketSettings = new ArrayList<>();
    public static List<BooleanComponent> sPacketSettings = new ArrayList<>();
    public static List<BooleanComponent> miscPacketSettings = new ArrayList<>();
    public static Map<Class<? extends Packet>, PacketData> dataMap = new HashMap<>();
    public final NumberSetting max = register(new NumberSetting("Max Lines", 16, 5, 100));
    private final BooleanSetting properties = register(new BooleanSetting("Properties", true));

    public PacketLog() {
        super(new PacketLogComponent(1, 11, 1, 1), "PacketLog", Category.HUD, "Shows the players ping.");
        ReflectionUtil.getSubclasses(Packet.class).forEach(packet -> dataMap.put(packet, new PacketData(packet)));
        // i dont wanna do this lol
        for(Map.Entry<Class<? extends Packet>, PacketData> entry : dataMap.entrySet()) {
            if(entry.getValue().setting.getName().startsWith("C")) {
                cPacketSettings.add(new BooleanComponent(entry.getValue().setting, false, getContainer().x, getContainer().y, getContainer().width, getContainer().height));
            }else if(entry.getValue().setting.getName().startsWith("S")) {
                sPacketSettings.add(new BooleanComponent(entry.getValue().setting, false, getContainer().x, getContainer().y, getContainer().width, getContainer().height));
            }else {
                miscPacketSettings.add(new BooleanComponent(entry.getValue().setting, false, getContainer().x, getContainer().y, getContainer().width, getContainer().height));
            }
        }
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
        PacketData data = dataMap.get(packet.getClass());
        if (data.setting.getValue()) {
            PacketLogComponent.packetHistory.add(data.name);
            if (properties.getValue()) {
                PacketLogComponent.packetHistory.addAll(data.getProperties(packet));
            }
        }
    }

    private class PacketData {
        private final List<ImmutablePair<String, Function<Object, String>>> properties = new ArrayList<>();
        public String name;
        public BooleanSetting setting;

        public PacketData(Class<? extends Packet> packet) {
            name = ReflectionUtil.betterSimpleName(packet);
            setting = register(new BooleanSetting(name, false));
            ReflectionUtil.allInstanceFields(packet).forEach(field ->
                    properties.add(new ImmutablePair<>(field.getName(), ReflectionUtil.fieldValue(field)))
            );
        }

        // Only pass same packet type as this was initialized with
        public List<String> getProperties(Packet<?> packet) {
            return properties.stream().map(pair ->
                    pair.left + ": " + pair.right.apply(packet)).collect(Collectors.toList()
            );
        }
    }
}
