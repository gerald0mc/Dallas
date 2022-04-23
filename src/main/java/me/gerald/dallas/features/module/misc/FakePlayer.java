package me.gerald.dallas.features.module.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class FakePlayer extends Module {
    public BooleanSetting moving = register(new BooleanSetting("Moving", true));
    public NumberSetting moveDelay = register(new NumberSetting("MoveDelay", 75, 25, 250, () -> moving.getValue()));
    public TimerUtil moveTimer = new TimerUtil();
    public EntityOtherPlayerMP fakePlayer;
    public Random random = new Random();
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

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!moving.getValue()) return;
        if (fakePlayer != null) {
            if (moveTimer.passedMs((long) moveDelay.getValue())) {
                if (mc.world.getBlockState(new BlockPos(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ)).getBlock() != Blocks.AIR)
                    fakePlayer.posY += 0.5f;
                else if (mc.world.getBlockState(new BlockPos(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ).down()).getBlock() == Blocks.AIR)
                    fakePlayer.posY -= 1;
                else {
                    int i = random.nextInt(2);
                    if (i == 0) {
                        fakePlayer.setPositionAndRotation(fakePlayer.posX + 0.25f, fakePlayer.posY, fakePlayer.posZ, fakePlayer.cameraYaw, fakePlayer.cameraPitch);
                    } else {
                        fakePlayer.setPositionAndRotation(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ + 0.25f, fakePlayer.cameraYaw, fakePlayer.cameraPitch);
                    }
                }
                moveTimer.reset();
            }
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
