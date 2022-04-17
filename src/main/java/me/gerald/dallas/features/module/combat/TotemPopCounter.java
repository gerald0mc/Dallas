package me.gerald.dallas.features.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TotemPopCounter extends Module {
    public TotemPopCounter() {
        super("TotemPopCounter", Category.COMBAT, "Says in chat when someone pops.");
    }

    public BooleanSetting notifications = register(new BooleanSetting("Notifications", true));

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if(event.getPopCount() == 1) {
            MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has popped a totem.", event.getEntity().getEntityId(), true);
            if(notifications.getValue())
                Yeehaw.INSTANCE.notificationManager.addNotification(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has popped a totem.", System.currentTimeMillis());
        }else if(event.getPopCount() > 1) {
            MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.GRAY + " totems.", event.getEntity().getEntityId(), true);
            if(notifications.getValue())
                Yeehaw.INSTANCE.notificationManager.addNotification(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.RESET + " totems.", System.currentTimeMillis());
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        MessageUtil.sendRemovableMessage(ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.GRAY + " has just died what a retard.", event.getEntity().getEntityId(), true);
        if(notifications.getValue())
            Yeehaw.INSTANCE.notificationManager.addNotification(ChatFormatting.BOLD + "Player Death", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has just died what a retard.", System.currentTimeMillis());
    }
}
