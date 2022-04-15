package me.gerald.dallas.features.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spammer extends Module {
    public NumberSetting delay = this.register(new NumberSetting("Delay", 3, 0.1f, 5));
    public BooleanSetting messageCounter = this.register(new BooleanSetting("MessageCounter", true));
    public TimerUtil timer = new TimerUtil();
    public List<String> messages = new ArrayList<>();
    public int messageCount = 0;
    String filePath = "Dallas" + File.separator + "Client" + File.separator + "Spammer.txt";

    public Spammer() {
        super("Spammer", Category.MISC, "Reads from a spammer file to spam chat.");
    }

    @Override
    public void onEnable() {
        File autoGGFile = new File(ConfigManager.clientPath, "Spammer.txt");
        if (!autoGGFile.exists()) {
            try {
                autoGGFile.createNewFile();
                MessageUtil.sendMessage("Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Spammer.txt" + ChatFormatting.RESET + " and add what you wish for it to say.");
                this.toggle();
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (this.timer.passedMs((long) (this.delay.getValue() * 1000))) {
            FileUtil.loadMessages(this.messages, this.filePath);
            mc.player.sendChatMessage(FileUtil.getRandomMessageWithDefault(this.messages, "I haven't added anything to my spammer yet!", this.filePath) + (this.messageCounter.getValue() ? " | " + this.messageCount : ""));
            this.messageCount++;
            this.timer.reset();
        }
    }

    @Override
    public void onDisable() {
        this.messageCount = 0;
    }
}
