package me.gerald.dallas.features.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", Category.MISC, "Says in chat when someone pops.");
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getPopCount() == 1) {
            MessageUtil.sendMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just popped a totem.");
        }else if(event.getPopCount() > 1) {
            MessageUtil.sendMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.GRAY + " totems.");
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        MessageUtil.sendMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just died what a retard.");
    }
}
