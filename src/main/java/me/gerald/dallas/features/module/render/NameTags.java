package me.gerald.dallas.features.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class NameTags extends Module {
    public NameTags() {
        super("NameTags", Category.RENDER, "Renders information above the player.");
    }

    public NumberSetting scale = register(new NumberSetting("Scale", 2.5f, 0.1f, 5.0f));
    public BooleanSetting ping = register(new BooleanSetting("Ping", true));
    public BooleanSetting health = register(new BooleanSetting("Health", true));

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityPlayer) {
                if(e == mc.player) return;
                EntityPlayer player = (EntityPlayer) e;
                double playerHealth = player.getHealth() + player.getAbsorptionAmount();
                String str = "";
                double yAdd = player.isSneaking() ? 1.75 : 2.25;
                double deltaX = MathHelper.clampedLerp(player.lastTickPosX, player.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(player.lastTickPosY, player.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(player.lastTickPosZ, player.posZ, event.getPartialTicks());
                Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, yAdd, 0));
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                str += Yeehaw.INSTANCE.friendManager.isFriend(player.getDisplayNameString()) ? ChatFormatting.AQUA + player.getDisplayNameString() + ChatFormatting.RESET : player.getDisplayNameString();
                if(ping.getValue())
                    str += " " + getPingColor(player) + MathHelper.ceil(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime()) + ChatFormatting.RESET + "ms";
                if(health.getValue())
                    str += " " + getHealthColor(player) + MathHelper.ceil(playerHealth) + ChatFormatting.RESET;
                Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, new Color(12, 12, 12, 100).getRGB());
                mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                GlStateManager.popMatrix();
            }
        }
    }

    public ChatFormatting getHealthColor(EntityPlayer entity) {
        if(entity.getHealth() + entity.getAbsorptionAmount() > 14) {
            return ChatFormatting.GREEN;
        }else if(entity.getHealth() + entity.getAbsorptionAmount() > 6){
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.RED;
        }
    }

    public ChatFormatting getPingColor(EntityPlayer entity) {
        if(Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).getPlayerInfo(entity.getUniqueID()).getResponseTime() > 150) {
            return ChatFormatting.RED;
        }else if(Minecraft.getMinecraft().getConnection().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 75) {
            return ChatFormatting.YELLOW;
        }else {
            return ChatFormatting.GREEN;
        }
    }
}
