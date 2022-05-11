package me.gerald.dallas.features.modules.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.PacketEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MathUtil;
import me.gerald.dallas.utils.MessageUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class FakePlayer extends Module {
    public BooleanSetting moving = new BooleanSetting("Moving", true);
    public NumberSetting moveDelay = new NumberSetting("MoveDelay", 75, 25, 250, () -> moving.getValue());
    public BooleanSetting popping = new BooleanSetting("Popping", true);
    public BooleanSetting particle = new BooleanSetting("Particle", true, () -> popping.getValue());
    public BooleanSetting sound = new BooleanSetting("Sound", false, () -> popping.getValue());
    public BooleanSetting distanceCheck = new BooleanSetting("DistanceCheck", true);
    public NumberSetting distance = new NumberSetting("Distance", 15, 1, 30, () -> distanceCheck.getValue());
    public BooleanSetting inventory = new BooleanSetting("Inventory", true);
    public ModeSetting inventoryMode = new ModeSetting("InventoryMode", "Player", () -> inventory.getValue(), "Player", "OP");
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
            if(inventory.getValue()) {
                switch (inventoryMode.getMode()) {
                    case "Player":
                        fakePlayer.inventory.copyInventory(mc.player.inventory);
                        break;
                    case "OP":
                        //TODO Add OP mode. (OP armor and totem)
                        break;
                }
            }
            mc.world.spawnEntity(fakePlayer);
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Fake Player", "Spawned a Fake Player.", true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (!moving.getValue()) return;
        if (fakePlayer != null) {
            if(mc.player.getDistance(fakePlayer) >= distance.getValue()) {
                if(distanceCheck.getValue())
                    fakePlayer.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            }
            if (popping.getValue()) {
                fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
                if (fakePlayer.getHealth() <= 0) {
                    fakePop(fakePlayer);
                    fakePlayer.setHealth(20f);
                    Yeehaw.INSTANCE.eventManager.totemPopListener.handlePop(fakePlayer);
                }
            }
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

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fakePlayer == null)
            return;
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion explosion = (SPacketExplosion) event.getPacket();
            if (fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15) {
                final double damage = MathUtil.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), fakePlayer);
                if (damage > 0 && popping.getValue())
                    fakePlayer.setHealth((float) (fakePlayer.getHealth() - MathHelper.clamp(damage,0, 999)));
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

    private void fakePop(Entity entity) {
        if(particle.getValue()) mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        if(sound.getValue()) mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
    }
}
