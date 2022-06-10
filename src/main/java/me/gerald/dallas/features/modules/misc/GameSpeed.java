package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.duck.IMinecraft;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class GameSpeed extends Module {
    public final NumberSetting speed = new NumberSetting("Speed", 1.0f, 0.1f, 20.0f, "How fast the timer is.");

    public GameSpeed() {
        super("GameSpeed", Category.MISC, "Timer with a different name.");
    }

    @Override
    public String getMetaData() {
        return String.valueOf(speed.getValue());
    }

    @Override
    public void onDisable() {
        ((IMinecraft) mc).setTimerSpeed(1.0f);
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event) {
        ((IMinecraft) mc).setTimerSpeed(speed.getValue());
    }
}
