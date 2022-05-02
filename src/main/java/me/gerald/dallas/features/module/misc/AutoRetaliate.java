package me.gerald.dallas.features.module.misc;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

public class AutoRetaliate extends Module {
    public NumberSetting delay = new NumberSetting("Delay", 100, 0, 1000);
    public ModeSetting targetMode = new ModeSetting("TargetMode", "Closest", "Closest", "Setting");
    public StringSetting targetName = new StringSetting("TargetName", "gerald0mc", () -> targetMode.getMode().equalsIgnoreCase("setting"));
    public TimerUtil timer = new TimerUtil();

    public AutoRetaliate() {
        super("AutoRetaliate", Category.MISC, "Does a thing.");
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        switch (targetMode.getMode()) {
            case "Closest":
                break;
            case "Setting":
                if (StringUtils.substringBetween(event.getMessage().getFormattedText(), "<", ">").equalsIgnoreCase(targetName.getValue())) {
                    if (timer.passedMs((long) delay.getValue())) {
                        mc.player.sendChatMessage("Cope");
                        timer.reset();
                        break;
                    }
                }
        }
    }
}
