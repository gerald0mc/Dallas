package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakePearl extends Module {
    private final Queue<CPacketPlayer> packets = new ConcurrentLinkedQueue<>();
    public boolean hasThrown = false;
    private int thrownPearlId = -1;

    public FakePearl() {
        super("FakePearl", Category.COMBAT, "When you throw a pearl it will be a fake pearl and will blink the player until the fake pearl has landed.");
    }

    @Override
    public String getMetaData() {
        return hasThrown ? ChatFormatting.GREEN + "ACTIVE" : "";
    }

    @SubscribeEvent
    public void onPacketR(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            if (packet.getType() == 65) {
                mc.world.playerEntities.stream()
                        .min(Comparator.comparingDouble((p) -> p.getDistance(packet.getX(), packet.getY(), packet.getZ())))
                        .ifPresent((player) -> {
                            if (player.equals(mc.player)) {
                                if (!mc.player.onGround)
                                    return;
                                // do not allow movement
                                mc.player.motionX = 0.0;
                                mc.player.motionY = 0.0;
                                mc.player.motionZ = 0.0;
                                mc.player.movementInput.moveForward = 0.0f;
                                mc.player.movementInput.moveStrafe = 0.0f;
                                // send rubberband packet
                                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ, false));
                                hasThrown = true;
                                thrownPearlId = packet.getEntityID();
                            }
                        });
            }
        }
    }

    @SubscribeEvent
    public void onPacketS(PacketEvent.Send event) {
        if (thrownPearlId != -1 && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packets.add(packet);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (thrownPearlId != -1) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity.getEntityId() == thrownPearlId && entity instanceof EntityEnderPearl) {
                    EntityEnderPearl pearl = (EntityEnderPearl) entity;
                    if (pearl.isDead) {
                        thrownPearlId = -1;
                        hasThrown = false;
                    }
                }
            }
        } else {
            if (!packets.isEmpty()) {
                do {
                    mc.player.connection.sendPacket(packets.poll());
                } while (!packets.isEmpty());
            }
        }
    }
}
