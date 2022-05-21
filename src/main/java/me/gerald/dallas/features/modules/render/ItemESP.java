package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemESP extends Module {
    public NumberSetting scale = new NumberSetting("Scale", 1, 0, 5);
    public BooleanSetting count = new BooleanSetting("Count", true);

    public ItemESP() {
        super("ItemESP", Category.RENDER, "Renders a items name and quantity.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (nullCheck()) return;
        for (Entity e : mc.world.getLoadedEntityList()) {
            if (e instanceof EntityItem) {
                EntityItem item = (EntityItem) e;
                if (item.getItem().getDisplayName().length() > 50) continue;
                double deltaX = MathHelper.clampedLerp(item.lastTickPosX, item.posX, event.getPartialTicks());
                double deltaY = MathHelper.clampedLerp(item.lastTickPosY, item.posY, event.getPartialTicks());
                double deltaZ = MathHelper.clampedLerp(item.lastTickPosZ, item.posZ, event.getPartialTicks());
                Vec3d projection = ProjectionUtil.toScaledScreenPos(new Vec3d(deltaX, deltaY, deltaZ).add(0, 0.25, 0));
                //Render stuff
                GlStateManager.pushMatrix();
                GlStateManager.translate(projection.x, projection.y, 0);
                GlStateManager.scale(scale.getValue(), scale.getValue(), 0);
                mc.fontRenderer.drawStringWithShadow((count.getValue() ? (item.getItem().getCount() == 1 ? "" : "x" + item.getItem().getCount() + " ") : "") + item.getItem().getDisplayName(), -(mc.fontRenderer.getStringWidth((count.getValue() ? (item.getItem().getCount() == 1 ? "" : "x" + item.getItem().getCount() + " ") : "") + item.getItem().getDisplayName()) / 2f), -(mc.fontRenderer.FONT_HEIGHT), -1);
                GlStateManager.popMatrix();
            }
        }
    }
}