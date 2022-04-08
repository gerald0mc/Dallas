package me.gerald.dallas.mod.mods.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtils;
import me.gerald.dallas.utils.TimerUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.legacydev.reloc.joptsimple.internal.Strings;
import org.apache.commons.lang3.StringUtils;

public class DallasBot extends Module {
    public DallasBot() {
        super("DallasBot", Category.CLIENT, "A bot with multiple useful commands.");
    }

    public NumberSetting coolDown = register(new NumberSetting("CoolDown(Seconds)", 5, 1, 30));
    public NumberSetting remindDelay = register(new NumberSetting("RemindDelay(Minutes)", 2.5f, 1, 5));

    public TimerUtils remindTimer = new TimerUtils();
    public TimerUtils coolDownTimer = new TimerUtils();
    public String afterMessage = " | Dallas Bot";

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        MessageUtils.sendMessage("For a full list of commands you can do with Dallas Bot please do dhelp.");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if(remindTimer.passedMs((long) (remindDelay.getValue() * 60000))) {
            mc.player.sendChatMessage("Remember you can do dhelp for a list of commands for Dallas Bot." + afterMessage);
            remindTimer.reset();
        }
    }

    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent event) {
        String[] strings = event.getMessage().getUnformattedText().split(" ");
        for(String string : strings) {
            if(string.equalsIgnoreCase("dhelp")) {
                if(StringUtils.substringBetween(event.getMessage().getUnformattedText(), "<", ">").equalsIgnoreCase(mc.player.getDisplayNameString())) {
                    MessageUtils.sendMessage(ChatFormatting.BLUE + "Da" + ChatFormatting.WHITE + "ll" + ChatFormatting.RED + "as" + ChatFormatting.GRAY + " Bot " + ChatFormatting.RESET + "Commands");
                    MessageUtils.sendMessage(ChatFormatting.GRAY + "[" + ChatFormatting.AQUA + "dhelp" + ChatFormatting.GRAY + "]: " +ChatFormatting.GREEN + "Shows bot commands that you and everyone else can do.");
                }else {
                    if(event.getMessage().getUnformattedText().contains("Dallas Bot")) return;
                    if(!coolDownTimer.passedMs((long) (coolDown.getValue() * 1000))) return;
                    mc.player.sendChatMessage("Dallas Bot Commands: [dhelp] says all commands" + afterMessage);
                    coolDownTimer.reset();
                }
            }
        }
    }
}
