package me.gerald.dallas.mod.mods.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ConfigManager;
import me.gerald.dallas.utils.FileUtils;
import me.gerald.dallas.utils.MessageUtils;
import me.gerald.dallas.utils.TimerUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spammer extends Module {
    public Spammer() {
        super("Spammer", Category.MISC, "Reads from a spammer file to spam chat.");
    }

    public NumberSetting delay = register(new NumberSetting("Delay", 3, 0.1f, 5));
    public BooleanSetting messageCounter = register(new BooleanSetting("MessageCounter", true));

    String filePath = "Dallas" + File.separator + "Client" + File.separator + "Spammer.txt";
    public TimerUtils timer = new TimerUtils();
    public List<String> messages = new ArrayList<>();
    public int messageCount = 0;

    @Override
    public void onEnable() {
        File autoGGFile = new File(ConfigManager.clientPath, "Spammer.txt");
        if(!autoGGFile.exists()) {
            try {
                autoGGFile.createNewFile();
                MessageUtils.sendMessage("Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Spammer.txt" + ChatFormatting.RESET + " and add what you wish for it to say.");
                toggle();
            }catch (IOException ignored) {}
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(timer.passedMs((long) (delay.getValue() * 1000))) {
            FileUtils.loadMessages(messages, filePath);
            mc.player.sendChatMessage(FileUtils.getRandomMessageWithDefault(messages, "I haven't added anything to my spammer yet!", filePath) + (messageCounter.getValue() ? " | " + messageCount : ""));
            messageCount++;
            timer.reset();
        }
    }

    @Override
    public void onDisable() {
        messageCount = 0;
    }
}
