package me.gerald.dallas.features.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.misc.NameChanger;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BlockUtil;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class Nametags extends Module {
    public NumberSetting scale = register(new NumberSetting("Scale", 2.5f, 0, 5));
    public BooleanSetting ping = register(new BooleanSetting("Ping", true));
    public BooleanSetting totemPops = register(new BooleanSetting("TotemPops", true));
    public BooleanSetting health = register(new BooleanSetting("Health", true));

    public Nametags() {
        super("Nametags", Category.RENDER, "Renders info about players.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player) continue;
            double playerHealth = player.getHealth() + player.getAbsorptionAmount();
            String str = "";
            double yAdd = player.isSneaking() ? 1.75 : 2.25;
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
            Gui.drawRect((int) -((mc.fontRenderer.getStringWidth(str) + 2) / 2f), -(mc.fontRenderer.FONT_HEIGHT + 2), ((mc.fontRenderer.getStringWidth(str) + 2) / (int) 2f), 1, new Color(12, 12, 12, 100).getRGB());
            mc.fontRenderer.drawStringWithShadow(str, -(mc.fontRenderer.getStringWidth(str) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
            GlStateManager.popMatrix();
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
