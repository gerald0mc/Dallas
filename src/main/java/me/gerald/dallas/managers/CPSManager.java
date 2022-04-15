package me.gerald.dallas.managers;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CPSManager {
    private final TimerUtil timer;
    private int usage;
    private int cps;

    public CPSManager() {
        MinecraftForge.EVENT_BUS.register(this);
        this.timer = new TimerUtil();
    }

    @SubscribeEvent
    public void onPacketS(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                this.usage++;
            } else if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                this.usage++;
            }
        }
    }

    public void calculateCPS() {
        if (!this.timer.passedMs(1000)) return;
        if (this.usage == 0) {
            this.cps = 0;
            this.timer.reset();
        } else {
            this.cps = this.usage;
            this.timer.reset();
            this.usage = 0;
        }
    }

    public int getCps() {
        return this.cps;
    }
}
