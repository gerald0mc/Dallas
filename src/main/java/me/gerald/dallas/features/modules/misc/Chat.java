package me.gerald.dallas.features.modules.misc;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class Chat extends Module {
    public BooleanSetting prefix = new BooleanSetting("Prefix", true);
    public StringSetting noPrefixWhen = new StringSetting("NoPrefixWhen", "/.!-=@#$*;", () -> prefix.getValue());
    public BooleanSetting emojis = new BooleanSetting("Emojis", true);

    public HashMap<String, String> emojiList;

    public Chat() {
        super("Chat", Category.MISC, "Chat things.");
        emojiList = new HashMap<>();
        emojiList.put("shrug", "\u00af\\_(\u30c4)_/\u00af");
        emojiList.put("disability", "\u267F");
        emojiList.put("tits", "\uA66D");
        emojiList.put("amoungus", "\u0D9E");
        emojiList.put("memeface", "(\u0361\u00B0\u035C\u0296\u0361\u00B0)");
        emojiList.put("middlefinger", "\u256D\u2229\u256E (-_-) \u256D\u2229\u256E");
        emojiList.put("texas", "\u24C9\u24BA\u24CD\u24B6\u24C8");
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String message = event.getOriginalMessage();
        if(prefix.getValue()) {
            String[] unsafe = noPrefixWhen.getValue().split("");
            for (String character : unsafe)
                if (event.getMessage().startsWith(character)) return;
            String suffix = " \u1D05\u1D00\u029F\u029F\u1D00\uA731";
            message += suffix;
        }
        if(emojis.getValue()) {
            for (Map.Entry<String, String> entry : emojiList.entrySet()) {
                if (message.contains(":" + entry.getKey() + ":")) {
                    message.replace(":" + entry.getKey() + ":", entry.getValue());
                }
            }
        }
        if(!message.equals(event.getOriginalMessage())) event.setMessage(message);
    }
}
