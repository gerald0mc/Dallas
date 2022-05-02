package me.gerald.dallas.features.module.misc;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chat extends Module {
    public StringSetting prefix = new StringSetting("NoPrefixWhen", "/.!-=@#$*;");

    public Chat() {
        super("Chat", Category.MISC, "Chat things. (Issues with chat module)");
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String[] unsafe = prefix.getValue().split("");
        for (String character : unsafe)
            if (event.getMessage().startsWith(character)) return;
        String suffix = " \u1D05\u1D00\u029F\u029F\u1D00\uA731";
        event.setMessage(event.getOriginalMessage() + suffix);
    }
}
