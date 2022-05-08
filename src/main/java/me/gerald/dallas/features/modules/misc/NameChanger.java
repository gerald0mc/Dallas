package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.setting.settings.StringSetting;
import me.gerald.dallas.utils.BlockUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NameChanger extends Module {
    public StringSetting name = new StringSetting("Name", "PERSON");
    public BooleanSetting mentionHighlight = new BooleanSetting("MentionHighlight", true);
    public BooleanSetting fakeClips = new BooleanSetting("FakeClips", false);
    public StringSetting fakeName = new StringSetting("FakeName", "WomanAreObjects", () -> fakeClips.getValue());
    public NumberSetting fakeDistance = new NumberSetting("FakeDistance", 30, 1, 50, () -> fakeClips.getValue());

    public NameChanger() {
        super("NameChanger", Category.MISC, "Changes the players name in chat.");
    }

    @Override
    public String getMetaData() {
        return "[" + ChatFormatting.WHITE + name.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        EntityPlayer closestPlayer = null;
        if (BlockUtil.findClosestPlayer().getDistance(mc.player) > fakeDistance.getValue() && BlockUtil.findClosestPlayer() != null && fakeClips.getValue())
            closestPlayer = BlockUtil.findClosestPlayer();
        if (event.getMessage().getUnformattedText().contains(mc.player.getDisplayNameString()))
            event.setMessage(new TextComponentString(event.getMessage().getFormattedText().replace(mc.player.getDisplayNameString(), (mentionHighlight.getValue() ? ChatFormatting.YELLOW : "") + name.getValue() + ChatFormatting.RESET)));
        if (event.getMessage().getUnformattedText().contains(closestPlayer.getDisplayNameString()) && fakeClips.getValue() && closestPlayer != null)
            event.setMessage(new TextComponentString(event.getMessage().getFormattedText().replace(closestPlayer.getDisplayNameString(), (mentionHighlight.getValue() ? ChatFormatting.YELLOW : "") + fakeName.getValue() + ChatFormatting.RESET)));
    }
}
