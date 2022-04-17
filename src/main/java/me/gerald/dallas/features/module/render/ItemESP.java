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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

public class ItemESP extends Module {
    public ItemESP() {
        super("ItemESP", Category.RENDER, "Renders a items name and quantity.");
    }

    public NumberSetting scale = register(new NumberSetting("Scale", 1, 0, 5));
    public BooleanSetting count = register(new BooleanSetting("Count", true));

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if(nullCheck()) return;
        for(Entity e : mc.world.getLoadedEntityList()) {
            if(e instanceof EntityItem) {
                EntityItem item = (EntityItem) e;
                double deltaX = MathHelper.clampedLerp(item.lastTickPosX, item.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(item.lastTickPosY, item.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(item.lastTickPosZ, item.posZ, event.getPartialTicks());
                Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, 0.25, 0));
                //Render stuff
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                mc.fontRenderer.drawStringWithShadow(count.getValue() ? (item.getItem().getCount() == 1 ? "" : "x" + item.getItem().getCount()) : "" + item.getItem().getDisplayName(), -(mc.fontRenderer.getStringWidth(item.getItem().getDisplayName()) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                GlStateManager.popMatrix();
            }
        }
    }
}
