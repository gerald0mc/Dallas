package me.gerald.dallas.mod.mods.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.DeathEvent;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ConfigManager;
import me.gerald.dallas.utils.MessageUtils;
import me.gerald.dallas.utils.TimerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AutoGG extends Module {
    public NumberSetting cooldown = register(new NumberSetting("Cooldown", 5, 1, 10));
    public BooleanSetting greenText = register(new BooleanSetting("GreenText", false));

    String filePath = "Dallas" + File.separator + "Client" + File.separator + "AutoGG.txt";
    public TimerUtils timer = new TimerUtils();
    public List<String> randomMessages = new ArrayList<>();

    public AutoGG() {
        super("AutoGG", Category.MISC, "Automatically tell someone how bad they are.");
    }

    @Override
    public void onEnable() {
        File autoGGFile = new File(ConfigManager.clientPath, "AutoGG.txt");
        if(!autoGGFile.exists()) {
            try {
                autoGGFile.createNewFile();
                MessageUtils.sendMessage("Please go into your " + ChatFormatting.GREEN + ".minecraft" + ChatFormatting.RESET + " folder and navigate to " + ChatFormatting.AQUA + "Dallas" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "Client" + ChatFormatting.GRAY + File.separator + ChatFormatting.AQUA + "AutoGG.txt" + ChatFormatting.RESET + " and add what you wish for it to say.");
            }catch (IOException ignored) {}
        }
    }

    @Override
    public void onDisable() {
        timer.reset();
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if(nullCheck()) return;
        if(event.getEntity().equals(mc.player)) return;
        if(!timer.passedMs((long) (cooldown.getValue() * 1000))) return;
        if(event.getEntity().getDistance(mc.player) > 30) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        mc.player.sendChatMessage(greenText.getValue() ? "> " : "" + getRandomMessage().replace("<player>", player.getDisplayNameString()));
        timer.reset();
    }

    public void loadMessages() {
        try {
            randomMessages.clear();
            Scanner s = new Scanner(new File(filePath));
            while (s.hasNextLine()){
                randomMessages.add(s.nextLine());
            }
            s.close();
        }catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public String getRandomMessage() {
        loadMessages();
        Random rand = new Random();
        if(randomMessages.size() == 0)
            return "Texas owns <player>!";
        if(randomMessages.size() == 1)
            return randomMessages.get(0);
        return randomMessages.get(clamp(rand.nextInt(randomMessages.size()), 0, randomMessages.size() - 1));
    }

    public int clamp(int num, int min, int max) {
        return (num < min) ? min : Math.min(num, max);
    }
}
