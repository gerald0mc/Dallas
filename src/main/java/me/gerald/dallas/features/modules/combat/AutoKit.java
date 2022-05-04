package me.gerald.dallas.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoKit extends Module {
    public StringSetting kitName = new StringSetting("KitName", "autoKit");
    public NumberSetting delay = new NumberSetting("Delay", 50, 0, 250);
    public boolean hasDied = false;
    public TimerUtil timer = new TimerUtil();

    public AutoKit() {
        super("AutoKit", Category.COMBAT, "Automatically does /kit + name.");
    }

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.WHITE + kitName.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getEntity() == mc.player && !hasDied)
            hasDied = true;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (hasDied && mc.player.isEntityAlive() && !mc.player.isDead && mc.player.getHealth() > 1) {
            if (timer.passedMs((long) delay.getValue())) {
                mc.player.sendChatMessage("/kit " + kitName.getValue());
                timer.reset();
                hasDied = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if (hasDied)
            hasDied = false;
    }
}
