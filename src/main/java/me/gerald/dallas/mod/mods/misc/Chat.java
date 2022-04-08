package me.gerald.dallas.mod.mods.misc;

import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.StringSetting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chat extends Module {
    public Chat() {
        super("Chat", Category.MISC, "Chat things.");
    }

    public StringSetting prefix = register(new StringSetting("NoPrefixWhen", "/.!-=@#$*;"));

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String[] unsafe = prefix.getValue().split("");
        for(String character : unsafe)
            if(event.getOriginalMessage().startsWith(character)) return;
        String suffix = " \u1D05\u1D00\u029F\u029F\u1D00\uA731";
        event.setMessage(event.getOriginalMessage() + suffix);
    }
}
