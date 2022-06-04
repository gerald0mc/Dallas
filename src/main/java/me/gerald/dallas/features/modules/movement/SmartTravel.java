package me.gerald.dallas.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.BaritoneHelper;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class SmartTravel extends Module {
    public SmartTravel() {
        super("SmartTravel", Category.MOVEMENT, "Will travel to a set of coords then log when reached.");
    }

    public boolean overrodeProcess = false;
    public BlockPos targetPos = null;

    @Override
    public void onEnable() {
        if (targetPos == null) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Please perform the travel comand and set your target XYZ location.", true);
            toggle();
            return;
        }
        if (!overrodeProcess) {
            overrodeProcess = true;
            BaritoneHelper.gotoPos(targetPos.getX(), targetPos.getZ());
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Starting to travel with Baritone...", true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if (!overrodeProcess) return;
        if (mc.player.getDistance(targetPos.getX(), mc.player.posY, targetPos.getZ()) <= 5) {
            overrodeProcess = false;
            BaritoneHelper.stop();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Arrived at location stopping Baritone and toggling.", true);
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged because you arrived at SmartTravel location.")));
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if (overrodeProcess) {
            overrodeProcess = false;
            BaritoneHelper.stop();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Stopping Baritone...", true);
        }
    }
}
