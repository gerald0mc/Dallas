package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Emojis extends Module {
    public HashMap<String, String> emojis;

    public Emojis() {
        super("Emoji", Category.MISC, "Sends emojis.");
        emojis = new HashMap<>();
        emojis.put("shrug", "\u00af\\_(\u30c4)_/\u00af");
        emojis.put("disability", "\u267F");
        emojis.put("tits", "\uA66D");
        emojis.put("amoungus", "\u0D9E");
        emojis.put("memeface", "(\u0361\u00B0\u035C\u0296\u0361\u00B0)");
        emojis.put("middlefinger", "\u256D\u2229\u256E (-_-) \u256D\u2229\u256E");
        emojis.put("texas", "\u24C9\u24BA\u24CD\u24B6\u24C8");
    }

    @Override
    public void onEnable() {
        File emojiFile = new File(ConfigManager.clientPath, "Emojis.txt");
        if (!emojiFile.exists()) {
            try {
                emojiFile.createNewFile();
                MessageUtil.sendMessage(ChatFormatting.BOLD + "Emojis", "Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Emojis.txt" + ChatFormatting.RESET + " and add what you wish for it to say.", true);
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH) //so it has higher priority over the chat modules.
    public void onChat(ClientChatEvent event) {
        for (Map.Entry<String, String> entry : emojis.entrySet()) {
            if (event.getMessage().contains(":" + entry.getKey() + ":")) {
                event.setMessage(event.getOriginalMessage().replace(":" + entry.getKey() + ":", entry.getValue()));
            }
        }
    }
}
