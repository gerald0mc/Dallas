package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", Category.COMBAT, "Says in chat when someone pops.");
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (event.getPopCount() == 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has popped a totem.", true);
        } else if (event.getPopCount() > 1) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.GRAY + " totems.", true);
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        MessageUtil.sendMessage(ChatFormatting.BOLD + "Player Death", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just died what a retard.", true);
    }
}
