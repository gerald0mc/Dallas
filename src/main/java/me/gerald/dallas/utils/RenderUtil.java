package me.gerald.dallas.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.awt.*;

public class RenderUtil {
    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL32.GL_DEPTH_CLAMP);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void release() {
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public static Color genRainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) (rainbowState % 360.0 / 360.0), 1f, 1f);
    }

    public static void renderItem(ItemStack stack, String string, int x, int y) {
        GL11.glPushMatrix();
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().zLevel = 200.0f;
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, stack, x, y, "");
        GlStateManager.enableTexture2D();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, x + 13, y + 10, -1);
        GL11.glPopMatrix();
    }

    /**
     * renderBorder - Renders a border with a changeable width and color around a set coordinate set.
     * renderBorderToggle - Renders a toggleable border with a changeable line width and color around a set coordinate set.
     * @param x The parameter for the X coordinate.
     * @param y The parameter for the Y coordinate.
     * @param width The width of the border. When writing in the constructor you should put it as (x + width).
     * @param height The height of the border. When writing in the constructor you should put it as (y + height).
     * @param lineWidth The width of the lines (Min value: 1).
     * @param color The color of the border lines.
     */

    public static void renderBorder(int x, int y, int width, int height, int lineWidth, Color color) {
        //top lines
        Gui.drawRect(x, y, width, y + lineWidth, color.getRGB());
        //left line
        Gui.drawRect(x, y, x + lineWidth, height, color.getRGB());
        //right line
        Gui.drawRect(width, y, width - lineWidth, height, color.getRGB());
        //bottom line
        Gui.drawRect(x, height, width, height - lineWidth, color.getRGB());
    }

    public static void renderBorderToggle(int x, int y, int width, int height, int lineWidth, Color color, boolean top, boolean left, boolean right, boolean bottom) {
        if(top)
            Gui.drawRect(x, y, width, y + lineWidth, color.getRGB());
        if(left)
            Gui.drawRect(x, y, x + lineWidth, height, color.getRGB());
        if(right)
            Gui.drawRect(width, y, width - lineWidth, height, color.getRGB());
        if(bottom)
            Gui.drawRect(x, height, width, height - lineWidth, color.getRGB());
    }
}
