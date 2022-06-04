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
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class FakePlayer extends Module {
    public BooleanSetting moving = new BooleanSetting("Moving", true, "Toggles the moving of the fake entity.");
    public NumberSetting moveDelay = new NumberSetting("MoveDelay", 75, 25, 250, "How fast the fake entity will move in milliseconds.", () -> moving.getValue());
    public BooleanSetting popping = new BooleanSetting("Popping", true, "Toggles popping of the fake entity.");
    public BooleanSetting particle = new BooleanSetting("Particle", true, "Toggles the particle of the totem pop.", () -> popping.getValue());
    public BooleanSetting sound = new BooleanSetting("Sound", false, "Toggles the sound of the totem pop.", () -> popping.getValue());
    public BooleanSetting distanceCheck = new BooleanSetting("DistanceCheck", true, "Toggles checking for distance from player.");
    public NumberSetting distance = new NumberSetting("Distance", 15, 1, 30, "The distance away from the player to teleport.", () -> distanceCheck.getValue());
    public BooleanSetting inventory = new BooleanSetting("Inventory", true, "Toggles the fake entity having a customizable inventory.");
    public ModeSetting inventoryMode = new ModeSetting("InventoryMode", "Player", "How the fake entities inventory will be created.", () -> inventory.getValue(), "Player", "OP");
    public BooleanSetting gapple = new BooleanSetting("Gapple", true, "Toggles the ability for fake entity to gapple.");
    public NumberSetting gappleDelay = new NumberSetting("GappleDelay(Secs)", 5, 1, 10, "Delay in seconds for gappling.", () -> gapple.getValue());

    public TimerUtil gappleTimer = new TimerUtil();
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
                        //diamond helmet
                        ItemStack helmet = new ItemStack(Items.DIAMOND_HELMET);
                        helmet.addEnchantment(Enchantments.PROTECTION, 4);
                        fakePlayer.inventory.armorInventory.set(3, helmet);
                        //diamond chest
                        ItemStack chest = new ItemStack(Items.DIAMOND_CHESTPLATE);
                        chest.addEnchantment(Enchantments.PROTECTION, 4);
                        fakePlayer.inventory.armorInventory.set(2, chest);
                        //diamond legs
                        ItemStack leggings = new ItemStack(Items.DIAMOND_LEGGINGS);
                        leggings.addEnchantment(Enchantments.BLAST_PROTECTION, 4);
                        fakePlayer.inventory.armorInventory.set(1, leggings);
                        //diamond boots
                        ItemStack boots = new ItemStack(Items.DIAMOND_BOOTS);
                        boots.addEnchantment(Enchantments.PROTECTION, 4);
                        fakePlayer.inventory.armorInventory.set(0, boots);
                        break;
                }
            }
            mc.world.spawnEntity(fakePlayer);
            gappleTimer.reset();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "Fake Player", "Spawned a Fake Player.", true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (fakePlayer != null) {
            if(mc.player.getDistance(fakePlayer) >= distance.getValue()) {
                if(distanceCheck.getValue())
                    fakePlayer.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.cameraYaw, mc.player.cameraPitch);
            }
            if(gapple.getValue()) {
                if(gappleTimer.passedMs((long) (int) gappleDelay.getValue() * 1000)) {
                    fakePlayer.setAbsorptionAmount(16.0f);
                    fakePlayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                    fakePlayer.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                    fakePlayer.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                    fakePlayer.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
                    gappleTimer.reset();
                }
            }
            if (popping.getValue()) {
                fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
                if (fakePlayer.getHealth() <= 0) {
                    fakePop(fakePlayer);
                    fakePlayer.setHealth(20f);
                    Yeehaw.INSTANCE.eventManager.totemPopListener.handlePop(fakePlayer);
                }
            }
            if(!moving.getValue()) return;
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
