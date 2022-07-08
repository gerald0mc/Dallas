package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class AutoTrader extends Module {
    public AutoTrader() {
        super("AutoTrader", Category.MISC, "Automatically trades for you.");
    }

    @SubscribeEvent
    public void onUpdate(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiMerchant) {
            if (!mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY)) return;
            NBTTagCompound tag = mc.objectMouseOver.entityHit.getEntityData();
            String tagString = tag.toString();
            System.out.println(tagString);
        }
    }

    @SubscribeEvent
    public void onPacketR(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketCustomPayload) {
            SPacketCustomPayload packet = (SPacketCustomPayload) event.getPacket();
            if (packet.getChannelName().equals("MC|TrList")) {
                System.out.println("BufferData: " + packet.getBufferData());
                System.out.println("ReadOnlyByteBuf: " + packet.getBufferData().asReadOnly());
                System.out.println("NioBuffer: " + packet.getBufferData().nioBuffer());
                System.out.println("NioBufferCount: " + packet.getBufferData().nioBufferCount());
                System.out.println("ByteArray: " + Arrays.toString(packet.getBufferData().array()));
                System.out.println("ByteArraySize: " + packet.getBufferData().array().length);
            }
        }
    }
}
