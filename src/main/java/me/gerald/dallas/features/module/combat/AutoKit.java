package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoKit extends Module {
    public StringSetting kitName = this.register(new StringSetting("KitName", "autoKit"));
    public NumberSetting delay = this.register(new NumberSetting("Delay", 50, 0, 250));
    public boolean hasDied = false;
    public TimerUtil timer = new TimerUtil();

    public AutoKit() {
        super("AutoKit", Category.COMBAT, "Automatically does /kit + name.");
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getEntity() == mc.player && !this.hasDied)
            this.hasDied = true;
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (this.hasDied && mc.player.isEntityAlive() && !mc.player.isDead && mc.player.getHealth() > 1) {
            if (this.timer.passedMs((long) this.delay.getValue())) {
                mc.player.sendChatMessage("/kit " + this.kitName.getValue());
                this.timer.reset();
                this.hasDied = false;
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.hasDied)
            this.hasDied = false;
    }
}
