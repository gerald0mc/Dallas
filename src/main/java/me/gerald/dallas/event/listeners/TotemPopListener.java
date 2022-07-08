package me.gerald.dallas.event.listeners;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class TotemPopListener {
    public HashMap<Entity, Integer> popMap = new HashMap<>();

    public TotemPopListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                if (!popMap.containsKey(packet.getEntity(Minecraft.getMinecraft().world))) {
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent(packet.getEntity(Minecraft.getMinecraft().world), 1));
                    popMap.put(packet.getEntity(Minecraft.getMinecraft().world), 1);
                } else {
                    popMap.put(packet.getEntity(Minecraft.getMinecraft().world), popMap.get(packet.getEntity(Minecraft.getMinecraft().world)) + 1);
                    MinecraftForge.EVENT_BUS.post(new TotemPopEvent(packet.getEntity(Minecraft.getMinecraft().world), popMap.get(packet.getEntity(Minecraft.getMinecraft().world))));
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null) {
            for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                if ((player.isDead || !player.isEntityAlive() || player.getHealth() <= 0)) {
                    popMap.remove(player);
                }
            }
        }
    }

    public int getTotalPops(Entity entity) {
        return popMap.getOrDefault(entity, 0);
    }

    public void handlePop(Entity entity) {
        if (!popMap.containsKey(entity)) {
            MinecraftForge.EVENT_BUS.post(new TotemPopEvent(entity, 1));
            popMap.put(entity, 1);
        } else {
            popMap.put(entity, popMap.get(entity) + 1);
            MinecraftForge.EVENT_BUS.post(new TotemPopEvent(entity, popMap.get(entity)));
        }
    }
}
