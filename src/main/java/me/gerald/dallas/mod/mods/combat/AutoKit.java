package me.gerald.dallas.mod.mods.combat;

import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class AutoKit extends Module {
    public AutoKit() {
        super("AutoKit", Category.COMBAT, "Automatically does /kit + name.");
    }

    public StringSetting name = register(new StringSetting("Name", "AutoKit"));

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if(event.player == mc.player) {
            if(mc.player.getHealth() > 0.0f) {
                mc.player.sendChatMessage("/kit " + name.getValue());
            }
        }
    }
}
