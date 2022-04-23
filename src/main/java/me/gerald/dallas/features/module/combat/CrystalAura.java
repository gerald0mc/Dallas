package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CrystalAura extends Module {
    public NumberSetting range = register(new NumberSetting("Range", 4, 1, 6));
    public NumberSetting delayMS = register(new NumberSetting("DelayMS", 0, 0, 500));
    public NumberSetting maxPlayerDamage = register(new NumberSetting("MaxPlayerDamage", 10, 1, 20));

    public TimerUtil placeTimer = new TimerUtil();
    public TimerUtil breakTimer = new TimerUtil();

    public CrystalAura() {
        super("CrystalAura", Module.Category.COMBAT, "NOT AUTO CRYSTAL!!!#!#!@#");
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 6.0F * 2.0F;
        double distancedSize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedSize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finalD = 1;
        if (entity instanceof EntityLivingBase) {
            finalD = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finalD;
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage = damage * (1.0F - f / 25.0F);

            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage = damage - (damage / 5);
            }

            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    //kami calcs

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        Entity target = getTarget((int) range.getValue());
        if (placeTimer.passedMs((long) delayMS.getValue()))
            placeLogic(target);
        if (breakTimer.passedMs((long) delayMS.getValue()))
            breakLogic();
    }

    public void placeLogic(Entity target) {
        List<BlockPos> blocks = findCrystalBlocks();
        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            for (BlockPos pos : blocks) {
                double playerDamage = calculateDamage(pos.getX(), pos.getY(), pos.getZ(), target);
                double selfDamage = calculateDamage(pos.getX(), pos.getY(), pos.getZ(), mc.player);
                if (playerDamage > .5) {
                    if (selfDamage > maxPlayerDamage.getValue()) return;
                    RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + .5, pos.getY() - .5d, pos.getZ() + .5));
                    EnumFacing face;
                    if (result == null || result.sideHit == null) {
                        face = EnumFacing.UP;
                    } else {
                        face = result.sideHit;
                    }
                    boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                    placeTimer.reset();
                    return;
                }
            }
        }
    }

    public void breakLogic() {
        for (Entity entity : mc.world.loadedEntityList) {
            if (mc.player.getDistance(entity) <= range.getValue()) {
                if (entity instanceof EntityEnderCrystal) {
                    mc.playerController.attackEntity(mc.player, entity);
                    breakTimer.reset();
                }
            }
        }
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockUtil.getSphere(mc.player.getPosition(), range.getValue(), false).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return positions;
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        return (mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK
                || mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN)
                && mc.world.getBlockState(boost).getBlock() == Blocks.AIR
                && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }

    public Entity getTarget(int range) {
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity == mc.player) continue;
            if (Yeehaw.INSTANCE.friendManager.isFriend(entity.getDisplayName().getUnformattedText())) continue;
            if (mc.player.getDistance(entity) < range) return entity;
        }
        return null;
    }

}
