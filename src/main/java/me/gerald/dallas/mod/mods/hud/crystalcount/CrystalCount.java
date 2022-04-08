package me.gerald.dallas.mod.mods.hud.crystalcount;

import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mod.HUDModule;
import me.gerald.dallas.utils.MessageUtils;
import me.gerald.dallas.utils.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrystalCount extends HUDModule {
    public CrystalCount() {
        super(new CrystalCountComponent(1, 11, 1, 1), "CrystalCount", Category.HUD, "Counts your crystal usage.");
    }

    public TimerUtils timer = new TimerUtils();
    public int usage;
    public int cps;

    @SubscribeEvent
    public void onPacketS(PacketEvent.Send event) {
        if(event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL || Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) return;
            MessageUtils.sendMessage("Usage increasing.");
            usage++;
        }
    }

    public void getCPS() {
        if(!timer.passedMs(1000)) return;
        if(usage == 0) {
            cps = 0;
            timer.reset();
        } else {
            cps = usage / 20;
            timer.reset();
        }
    }
}
