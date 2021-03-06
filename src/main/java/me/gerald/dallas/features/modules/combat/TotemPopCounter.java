package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.event.events.TotemPopEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TotemPopCounter extends Module {
    public ModeSetting messageMode = new ModeSetting("MessageMode", "Default", "Visual style of the messages being sent.", "Default", "Basic");

    public TotemPopCounter() {
        super("TotemPopCounter", Category.COMBAT, "Says in chat when someone pops.");
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        if (event.getPopCount() == 1) {
            switch (messageMode.getMode()) {
                case "Default":
                    MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has popped a totem.", event.getEntity().getEntityId(), MessageUtil.MessageType.GENERAL);
                    return;
                case "Basic":
                    MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " popped " + ChatFormatting.RED + "1", event.getEntity().getEntityId(), MessageUtil.MessageType.GENERAL);
            }
        } else if (event.getPopCount() > 1) {
            switch (messageMode.getMode()) {
                case "Default":
                    MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has popped " + ChatFormatting.RED + event.getPopCount() + ChatFormatting.RESET + " totems.", event.getEntity().getEntityId(), MessageUtil.MessageType.GENERAL);
                    return;
                case "Basic":
                    MessageUtil.sendRemovableMessage(ChatFormatting.BOLD + "Totem Pop", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " popped " + ChatFormatting.RED + event.getPopCount(), event.getEntity().getEntityId(), MessageUtil.MessageType.GENERAL);
            }
        }
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        switch (messageMode.getMode()) {
            case "Default":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Player Death", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " has just died what a retard.", MessageUtil.MessageType.GENERAL);
                return;
            case "Basic":
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Player Death", ChatFormatting.AQUA + event.getEntity().getDisplayName().getFormattedText() + ChatFormatting.RESET + " died.", MessageUtil.MessageType.GENERAL);
        }
    }
}
