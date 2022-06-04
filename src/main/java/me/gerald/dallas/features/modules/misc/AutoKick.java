package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.client.CPacketPlayer;

public class AutoKick extends Module {
    public AutoKick() {
        super("AutoKick", Category.MISC, "Kicks you on demand. (Tested on CC)");
    }

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        ServerData data = mc.getCurrentServerData();
        if(data == null) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "AutoKick", "You are not on a server! Toggling without performing any action.", true);
            toggle();
            return;
        }
        for(int i = 0; i < 10; i++)
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, -1337.69, mc.player.posZ, false));
        toggle();
    }
}
