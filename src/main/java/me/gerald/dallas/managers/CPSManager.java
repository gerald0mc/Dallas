package me.gerald.dallas.managers;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CPSManager {
    public CPSManager() {
        MinecraftForge.EVENT_BUS.register(this);
        timer = new TimerUtil();
    }

    private TimerUtil timer;
    private int usage;
    private int cps;

    @SubscribeEvent
    public void onPacketS(PacketEvent.Send event) {
        if(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                usage++;
                MessageUtil.sendMessage("Usage increasing");
            }else if(Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                usage++;
                MessageUtil.sendMessage("Usage increasing");
            }
        }
    }

    public void calculateCPS() {
        if(!timer.passedMs(1000)) return;
        if(usage == 0) {
            cps = 0;
            MessageUtil.sendMessage("Set cps to 0");
            timer.reset();
        } else {
            cps = usage / 20;
            MessageUtil.sendMessage("Set cps to " + usage / 20);
            timer.reset();
            usage = 0;
        }
    }

    public int getCps() {
        return cps;
    }
}
