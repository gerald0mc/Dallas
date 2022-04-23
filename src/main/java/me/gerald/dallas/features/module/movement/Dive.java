package me.gerald.dallas.features.module.movement;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Dive extends Module {
    public BooleanSetting autoDisable = register(new BooleanSetting("AutoDisable", true));
    public NumberSetting bypassAmount = register(new NumberSetting("BypassAmount", -1, -10, 10));

    public Dive() {
        super("Dive", Category.MOVEMENT, "Cool dolphin thing.");
    }

    @Override
    public void onEnable() {
        mc.player.jump();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.01D, mc.player.posZ, mc.player.onGround));
        for (int i = 0; i < 2; i++)
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + bypassAmount.getValue(), mc.player.posZ, mc.player.onGround));
        if (autoDisable.getValue())
            toggle();
    }
}
