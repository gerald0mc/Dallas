package me.gerald.dallas.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.modules.misc.NameChanger;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class Nametags extends Module {
    public NumberSetting scale = new NumberSetting("Scale", 2, 0, 5);
    public BooleanSetting backGround = new BooleanSetting("BackGround", true);
    public BooleanSetting border = new BooleanSetting("Border", true, () -> backGround.getValue());
    public BooleanSetting clientSync = new BooleanSetting("ClientSync", true, () -> border.getValue());
    public ColorSetting borderColor = new ColorSetting("BorderColor", 0, 0, 0, 255, () -> !clientSync.getValue());
    public BooleanSetting ping = new BooleanSetting("Ping", true);
    public BooleanSetting totemPops = new BooleanSetting("TotemPops", true);
    public BooleanSetting health = new BooleanSetting("Health", true);
    public BooleanSetting allEntities = new BooleanSetting("AllEntities", true);
    public BooleanSetting entityHealth = new BooleanSetting("EntityHealth", true, () -> allEntities.getValue());
    public BooleanSetting animals = new BooleanSetting("Animals", true, () -> allEntities.getValue());
    public BooleanSetting mobs = new BooleanSetting("Mobs", true, () -> allEntities.getValue());

    public Nametags() {
        super("Nametags", Category.RENDER, "Renders info about players.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        Color clientColor = ClickGUI.clientColor;
        for (Entity e : mc.world.loadedEntityList) {
            if (e == mc.player) continue;
            if(e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;
                if(e instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) e;
                    double playerHealth = player.getHealth() + player.getAbsorptionAmount();
                    String str = "";
                    double yAdd = e.getEyeHeight() + .5f;
                    double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
                    double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
                    double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());
                    Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                    //Render stuff
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(projection.x, projection.y, 0);
                    GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                    if (Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).fakeClips.getValue() && Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).isEnabled() && !Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) && BlockUtil.findClosestPlayer() == player)
                        str += Yeehaw.INSTANCE.moduleManager.getModule(NameChanger.class).fakeName.getValue();
                    else
                        str += Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) ? ChatFormatting.AQUA + player.getDisplayNameString() + ChatFormatting.RESET : player.getDisplayNameString();
                    if (ping.getValue())
                        str += " " + ChatFormatting.GRAY + MathHelper.ceil(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime()) + "ms";
                    if (totemPops.getValue())
                        str += " Pops: " + Yeehaw.INSTANCE.eventManager.totemPopListener.getTotalPops(player);
                    if (health.getValue())
                        str += " " + getHealthColor(player) + MathHelper.ceil(playerHealth) + ChatFormatting.RESET;
                    if(backGround.getValue()) {
                        Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, new Color(12, 12, 12, 100).getRGB());
                        if(border.getValue())
                            RenderUtil.renderBorder((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, 1, clientSync.getValue() ? clientColor : borderColor.getColor());
                    }
                    mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                    GlStateManager.popMatrix();
                } else if (allEntities.getValue()) {
                    if(entity instanceof EntityAnimal && !animals.getValue()) return;
                    else if((entity instanceof EntityMob || entity instanceof EntitySlime) && !mobs.getValue()) return;
                    String eStr = "";
                    double health = entity.getHealth() + entity.getAbsorptionAmount();
                    double yAdd = e.getEyeHeight() + .5f;
                    double deltaX = MathHelper.clampedLerp(entity.lastTickPosX, entity.posX, event.getPartialTicks());
                    double deltaY = MathHelper.clampedLerp(entity.lastTickPosY, entity.posY, event.getPartialTicks());
                    double deltaZ = MathHelper.clampedLerp(entity.lastTickPosZ, entity.posZ, event.getPartialTicks());
                    Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                    //Render stuff
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(projection.x, projection.y, 0);
                    GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                    eStr += entity.getDisplayName().getFormattedText() + (entityHealth.getValue() ? " " + MathHelper.ceil(health) : "");
                    if(backGround.getValue()) {
                        Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(eStr) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(eStr) + 2) / (int) 2f), 1, new Color(12, 12, 12, 100).getRGB());
                        if(border.getValue())
                            RenderUtil.renderBorder((int) -((mc.fontRenderer.getStringWidth(eStr) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(eStr) + 2) / (int) 2f), 1, 1, clientSync.getValue() ? clientColor : borderColor.getColor());
                    }
                    mc.fontRenderer.drawStringWithShadow(eStr, -(mc.fontRenderer.getStringWidth(eStr) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    public ChatFormatting getHealthColor(EntityPlayer entity) {
        if (entity.getHealth() + entity.getAbsorptionAmount() > 14) {
            return ChatFormatting.GREEN;
        } else if (entity.getHealth() + entity.getAbsorptionAmount() > 6) {
            return ChatFormatting.YELLOW;
        } else {
            return ChatFormatting.RED;
        }
    }
}
