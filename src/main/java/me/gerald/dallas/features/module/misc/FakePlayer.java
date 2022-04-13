package me.gerald.dallas.features.module.misc;

import com.mojang.authlib.GameProfile;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Module {
    public FakePlayer() {
        super("FakePlayer", Category.MISC, "Spawns a fake player into the world.");
    }

    public EntityOtherPlayerMP fakePlayer;

    @Override
    public void onEnable() {
        if(nullCheck()) return;
        if(fakePlayer == null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(mc.player.getUniqueID(), "Dallas"));
            fakePlayer.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            mc.world.spawnEntity(fakePlayer);
            MessageUtil.sendMessage("Spawned a Fake Player.");
        }
    }

    @Override
    public void onDisable() {
        if(nullCheck()) return;
        if(fakePlayer != null) {
            mc.world.removeEntity(fakePlayer);
            fakePlayer = null;
        }
    }
}
