package me.gerald.dallas.features.module.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.StringUtils;

public class DallasBot extends Module {
    public NumberSetting coolDown = register(new NumberSetting("CoolDown(Secs)", 5, 1, 30));
    public NumberSetting remindDelay = register(new NumberSetting("RemindDelay(Mins)", 2.5f, 1, 5));
    public TimerUtil remindTimer = new TimerUtil();
    public TimerUtil coolDownTimer = new TimerUtil();
    public String afterMessage = " | Dallas Bot";
    public DallasBot() {
        super("DallasBot", Category.CLIENT, "A bot with multiple useful commands.");
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;
        MessageUtil.sendMessage("For a full list of commands you can do with Dallas Bot please do dhelp.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (remindTimer.passedMs((long) (remindDelay.getValue() * 60000))) {
            mc.player.sendChatMessage("Remember you can do dhelp for a list of commands for Dallas Bot." + afterMessage);
            remindTimer.reset();
        }
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        String[] strings = event.getMessage().getUnformattedText().split(" ");
        for (String string : strings) {
            if (string.equalsIgnoreCase("dhelp")) {
                if (StringUtils.substringBetween(event.getMessage().getUnformattedText(), "<", ">").equalsIgnoreCase(mc.player.getDisplayNameString())) {
                    MessageUtil.sendMessage(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " Bot " + ChatFormatting.RESET + "Commands");
                    MessageUtil.sendMessage(ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "dhelp" + ChatFormatting.GRAY + "]: " + ChatFormatting.GREEN + "Shows bot commands that you and everyone else can do.");
                } else {
                    if (event.getMessage().getUnformattedText().contains("Dallas Bot")) return;
                    if (!coolDownTimer.passedMs((long) (coolDown.getValue() * 1000))) return;
                    mc.player.sendChatMessage("Dallas Bot Commands: [dhelp] says all commands" + afterMessage);
                    coolDownTimer.reset();
                }
            }
        }
    }
}
