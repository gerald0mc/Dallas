package me.gerald.dallas.features.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.managers.ConfigManager;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.FileUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutoGG extends Module {
    public NumberSetting cooldown = register(new NumberSetting("Cooldown(Secs)", 5, 1, 10));
    public BooleanSetting greenText = register(new BooleanSetting("GreenText", false));
    public TimerUtil timer = new TimerUtil();
    public List<String> randomMessages = new ArrayList<>();
    String filePath = "Dallas" + File.separator + "Client" + File.separator + "AutoGG.txt";

    public AutoGG() {
        super("AutoGG", Category.MISC, "Automatically tell someone how bad they are.");
    }

    @Override
    public void onEnable() {
        File autoGGFile = new File(ConfigManager.clientPath, "AutoGG.txt");
        if (!autoGGFile.exists()) {
            try {
                autoGGFile.createNewFile();
                MessageUtil.sendMessage("Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "AutoGG.txt" + ChatFormatting.RESET + " and add what you wish for it to say.");
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (nullCheck()) return;
        if (event.getEntity().equals(mc.player)) return;
        if (!timer.passedMs((long) (cooldown.getValue() * 1000))) return;
        if (event.getEntity().getDistance(mc.player) > 30) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        mc.player.sendChatMessage(greenText.getValue() ? "> " : "" + FileUtil.getRandomMessageWithDefault(randomMessages, "Texas owns <player>!", filePath).replace("<player>", player.getDisplayNameString()));
        timer.reset();
    }
}
