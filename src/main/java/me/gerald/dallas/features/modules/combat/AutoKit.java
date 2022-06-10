package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoKit extends Module {
    public StringSetting kitName = new StringSetting("KitName", "autoKit", "Name of the kit.");
    public NumberSetting delay = new NumberSetting("Delay", 50, 0, 250, "How fast it will perform the kit command.");
    public BooleanSetting adapt = new BooleanSetting("Adapt", true, "Will change current kit name when the user performs their own kit.");
    public BooleanSetting noServerToggle = new BooleanSetting("NoServerToggle", true, "Will toggle the module if you are not on a server.");

    public TimerUtil timer = new TimerUtil();
    public boolean hasDied = false;

    public AutoKit() {
        super("AutoKit", Category.COMBAT, "Automatically does /kit + name.");
    }

    @Override
    public String getMetaData() {
        return kitName.getValue();
    }

    @SubscribeEvent
    public void onChatR(ClientChatEvent event) {
        if (!adapt.getValue()) return;
        String[] messageList = event.getMessage().split(" ");
        String newKitName = "";
        if (messageList[0].equalsIgnoreCase("/kit")) {
            if (messageList.length == 2) {
                newKitName = messageList[1];
            } else {
                for (int i = 1; i < messageList.length; i++) {
                    if (i + 1 == messageList.length + 1) {
                        newKitName += messageList[i];
                        break;
                    } else {
                        newKitName += messageList[i] + " ";
                    }
                }
            }
            if (newKitName.equalsIgnoreCase(kitName.getValue())) return;
            kitName.setValue(newKitName);
            MessageUtil.sendMessage(ChatFormatting.BOLD + "AutoKit", "Set kit value to " + ChatFormatting.AQUA + newKitName, MessageUtil.MessageType.CONSTANT);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        ServerData data = mc.getCurrentServerData();
        if (data == null) {
            if (!noServerToggle.getValue()) return;
            MessageUtil.sendMessage(ChatFormatting.BOLD + "AutoKit", "Not on a server toggling module...", MessageUtil.MessageType.CONSTANT);
            toggle();
        }
        if (hasDied && mc.player.isEntityAlive() && !mc.player.isDead && mc.player.getHealth() > 1) {
            if (timer.passedMs((long) delay.getValue())) {
                mc.player.sendChatMessage("/kit " + kitName.getValue());
                hasDied = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if (hasDied)
            hasDied = false;
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getEntity() == mc.player && !hasDied) {
            timer.reset();
            hasDied = true;
        }
    }
}
