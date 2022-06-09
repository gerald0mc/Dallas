package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatPrefix extends Module {
    public StringSetting noPrefixWhen = new StringSetting("NoPrefixWhen", "/.!-=@#$*;", "What characters to not add prefix to when sending a message.");

    public ChatPrefix() {
        super("ChatPrefix", Category.MISC, "Adds a client abbreviation to your messages.");
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String[] unsafe = noPrefixWhen.getValue().split("");
        for (String character : unsafe)
            if (event.getMessage().startsWith(character)) return;
        String suffix = " \u1D05\u1D00\u029F\u029F\u1D00\uA731";
        event.setMessage(event.getMessage() + suffix);
    }
}
