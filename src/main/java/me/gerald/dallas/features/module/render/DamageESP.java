package me.gerald.dallas.features.module.render;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.mixin.mixins.IEntityRenderer;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DamageESP extends Module {
    public NumberSetting timeBetweenChecks = this.register(new NumberSetting("TimeBetweenChecks", 1.0f, 0.1f, 5.0f));
    public NumberSetting timeToRemove = this.register(new NumberSetting("TimeToRemove", 3, 1, 5));
    public BooleanSetting self = this.register(new BooleanSetting("Self", true));
    public BooleanSetting healText = this.register(new BooleanSetting("HealText", true));
    public ColorSetting healColor = this.register(new ColorSetting("HealColor", 0, 255, 0, 255, () -> this.healText.getValue()));
    public BooleanSetting damageText = this.register(new BooleanSetting("DamageText", true));
    public ColorSetting damageColor = this.register(new ColorSetting("DamageColor", 255, 0, 0, 255, () -> this.damageText.getValue()));
    public HashMap<EntityLivingBase, Float> entityHealthMap = new HashMap<>();
    public List<Damage> damages = new ArrayList<>();
    public TimerUtil timer = new TimerUtil();

    public DamageESP() {
        super("DamageESP", Category.RENDER, "DamageESP");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        for (Entity e : mc.world.loadedEntityList) {
            if (e == mc.player && !this.self.getValue()) continue;
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;
                if (!this.entityHealthMap.containsKey(entity)) {
                    this.entityHealthMap.put(entity, entity.getHealth());
                } else {
                    if (this.entityHealthMap.get(entity) > entity.getHealth()) {
                        if (!this.timer.passedMs((long) (this.timeBetweenChecks.getValue() * 1000))) return;
                        this.damages.add(new Damage(e, System.currentTimeMillis(), (this.entityHealthMap.get(entity) - entity.getHealth()), (float) ThreadLocalRandom.current().nextDouble(-0.5, 1.0), 1));
                        this.entityHealthMap.replace(entity, entity.getHealth());
                        this.timer.reset();
                    } else if (this.entityHealthMap.get(entity) < entity.getHealth()) {
                        if (!this.timer.passedMs((long) (this.timeBetweenChecks.getValue() * 1000))) return;
                        this.damages.add(new Damage(e, System.currentTimeMillis(), (entity.getHealth() - this.entityHealthMap.get(entity)), (float) ThreadLocalRandom.current().nextDouble(-0.5, 1.0), 2));
                        this.entityHealthMap.replace(entity, entity.getHealth());
                        this.timer.reset();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (this.damages.isEmpty()) return;
        for (Damage damage : this.damages) {
            if (System.currentTimeMillis() - damage.startTime >= this.timeToRemove.getValue() * 1000) {
                this.damages.remove(damage);
                return;
            }
            final double x = damage.getEntity().getPosition().getX() + (damage.getEntity().getPosition().getX() - damage.getEntity().getPosition().getX()) * event.getPartialTicks() - mc.getRenderManager().viewerPosX;
            final double y = damage.getEntity().getPosition().getY() + (damage.getEntity().getPosition().getY() - damage.getEntity().getPosition().getY()) * event.getPartialTicks() - mc.getRenderManager().viewerPosY + damage.getEntity().getEyeHeight() + damage.getHeight();
            final double z = damage.getEntity().getPosition().getZ() + (damage.getEntity().getPosition().getZ() - damage.getEntity().getPosition().getZ()) * event.getPartialTicks() - mc.getRenderManager().viewerPosZ;
            final float var10001 = (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            ((IEntityRenderer) mc.entityRenderer).setupCameraTransformInvoker(event.getPartialTicks(), 0);
            GL11.glTranslated(x, y, z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(mc.getRenderManager().playerViewX, var10001, 0.0f, 0.0f);
            GL11.glScaled(-0.041666668839752674, -0.041666668839752674, 0.041666668839752674);
            final long timeLeft = damage.startTime + 1000L - System.currentTimeMillis();
            float yPercentage;
            float sizePercentage;
            if (timeLeft < 75L) {
                sizePercentage = Math.min(timeLeft / 75.0f, 1.0f);
                yPercentage = Math.min(timeLeft / 75.0f, 1.0f);
            } else {
                sizePercentage = Math.min((System.currentTimeMillis() - damage.startTime) / 300.0f, 1.0f);
                yPercentage = Math.min((System.currentTimeMillis() - damage.startTime) / 600.0f, 1.0f);
            }
            GlStateManager.scale(0.8 * sizePercentage, 0.8 * sizePercentage, 0.8 * sizePercentage);
            Gui.drawRect(-100, -100, 100, 100, new Color(255, 0, 0, 0).getRGB());
            //font is a cuatom font
            switch (damage.getStage()) {
                case 2:
                    if (!this.healText.getValue()) return;
                    mc.fontRenderer.drawStringWithShadow(new DecimalFormat("#.#").format(damage.damage), 0, (int) (-yPercentage), this.healColor.getColor().getRGB());
                    break;
                case 1:
                    if (!this.damageText.getValue()) return;
                    mc.fontRenderer.drawStringWithShadow(new DecimalFormat("#.#").format(damage.damage), 0, (int) (-yPercentage), this.damageColor.getColor().getRGB());
                    break;
            }
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }

    public static class Damage {
        private final Entity entity;
        private final long startTime;
        private final float damage;
        private final float height;
        private final int stage;

        public Damage(Entity entity, long startTime, float damage, float height, int stage) {
            this.entity = entity;
            this.startTime = startTime;
            this.damage = damage;
            this.height = height;
            this.stage = stage;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public long getStartTime() {
            return this.startTime;
        }

        public float getDamage() {
            return this.damage;
        }

        public float getHeight() {
            return this.height;
        }

        public int getStage() {
            return this.stage;
        }
    }
}
