package me.gerald.dallas.features.module.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Module {
    public EntityOtherPlayerMP fakePlayer;

    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Spawns a fake player into the world.");
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;
        if (fakePlayer == null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), "Dallas"));
            fakePlayer.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            mc.world.spawnEntity(fakePlayer);
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Fake Player", "Spawned a Fake Player.", true);
        }
    }

    @Override
    public void onDisable() {
        if (nullCheck()) return;
        if (fakePlayer != null) {
            mc.world.removeEntity(fakePlayer);
            fakePlayer = null;
        }
    }
}
