package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.event.events.DamageEvent;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.mixins.IEntityRenderer;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DamageESP extends Module {
    public NumberSetting timeBetweenChecks = new NumberSetting("TimeBetweenChecks", 1.0f, 0.1f, 5.0f, "Time in seconds between each health check.");
    public BooleanSetting self = new BooleanSetting("Self", true, "Toggles the checking and rendering of the player. (YOU)");
    public BooleanSetting healText = new BooleanSetting("HealText", true, "Toggles rendering of the heal text.");
    public ColorSetting healColor = new ColorSetting("HealColor", 0, 255, 0, 255, "What color the heal text is gonna be.", () -> healText.getValue());
    public BooleanSetting damageText = new BooleanSetting("DamageText", true, "Toggles rendering of the damage text.");
    public ColorSetting damageColor = new ColorSetting("DamageColor", 255, 0, 0, 255, "What color the damage text is gonna be.", () -> damageText.getValue());

    public List<Damage> damages = new ArrayList<>();

    public DamageESP() {
        super("DamageESP", Category.RENDER, "DamageESP");
    }

    @SubscribeEvent
    public void onUpdate(DamageEvent.Damage event) {
        if (nullCheck()) return;
        if (event.getEntity().equals(mc.player) && !self.getValue()) return;
        damages.add(new Damage(event.getEntity(), System.currentTimeMillis(), (float) event.getAmount(), (float) ThreadLocalRandom.current().nextDouble(-0.5, 1), 1));
    }

    @SubscribeEvent
    public void onUpdate(DamageEvent.Heal event) {
        if (nullCheck()) return;
        if (event.getEntity().equals(mc.player) && !self.getValue()) return;
        damages.add(new Damage(event.getEntity(), System.currentTimeMillis(), (float) event.getAmount(), (float) ThreadLocalRandom.current().nextDouble(-0.5, 1), 1));
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (nullCheck()) return;
        if (damages.isEmpty()) return;
        for (Damage damage : damages) {
            if (System.currentTimeMillis() - damage.startTime >= 1000) {
                damages.remove(damage);
                return;
            }
            final double x = damage.getEntity().getPosition().getX() + (damage.getEntity().getPosition().getX() - damage.getEntity().getPosition().getX()) * event.getPartialTicks() - mc.getRenderManager().viewerPosX;
            final double y = damage.getEntity().getPosition().getY() + (damage.getEntity().getPosition().getY() - damage.getEntity().getPosition().getY()) * event.getPartialTicks() - mc.getRenderManager().viewerPosY + damage.getEntity().getEyeHeight() + damage.getHeight();
            final double z = damage.getEntity().getPosition().getZ() + (damage.getEntity().getPosition().getZ() - damage.getEntity().getPosition().getZ()) * event.getPartialTicks() - mc.getRenderManager().viewerPosZ;
            final float var10001 = (mc.gameSettings.thirdPersonView == 2) ? -1 : 1;
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
            if (timeLeft < 75) {
                sizePercentage = Math.min(timeLeft / 75, 1);
                yPercentage = Math.min(timeLeft / 75, 1);
            } else {
                sizePercentage = Math.min((System.currentTimeMillis() - damage.startTime) / 300, 1);
                yPercentage = Math.min((System.currentTimeMillis() - damage.startTime) / 600, 1);
            }
            GlStateManager.scale(0.8 * sizePercentage, 0.8 * sizePercentage, 0.8 * sizePercentage);
            Gui.drawRect(-100, -100, 100, 100, new Color(255, 0, 0, 0).getRGB());
            switch (damage.getStage()) {
                case 2:
                    if (!healText.getValue()) return;
                    mc.fontRenderer.drawStringWithShadow(new DecimalFormat("#.#").format(damage.damage), 0, (int) (-yPercentage), healColor.getColor().getRGB());
                    break;
                case 1:
                    if (!damageText.getValue()) return;
                    mc.fontRenderer.drawStringWithShadow(new DecimalFormat("#.#").format(damage.damage), 0, (int) (-yPercentage), damageColor.getColor().getRGB());
                    break;
            }
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GlStateManager.color(1, 1, 1);
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
            return entity;
        }

        public long getStartTime() {
            return startTime;
        }

        public float getDamage() {
            return damage;
        }

        public float getHeight() {
            return height;
        }

        public int getStage() {
            return stage;
        }
    }
}
