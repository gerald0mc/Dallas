package me.gerald.dallas.utils;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class TextureUtil implements Globals {
    /**
     * Binds the resource location to the texture manager
     * @param location the resource location
     */
    public static void bindTexture(ResourceLocation location) {
        mc.getTextureManager().bindTexture(location);
    }

    public static void bindTexture(int textureId) {
        GlStateManager.bindTexture(textureId);
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    /**
     * Draws a texture on the screen
     *
     * @param location the texture's resource location
     * @param x the x coordinate
     * @param y the y coordinate
     * @param u the u coorinate on the texture
     * @param v the v coordinate on the texture
     * @param height the height of the texture
     * @param width the width of the texture
     */
    public static void drawTexture(ResourceLocation location, double x, double y, int u, int v, double height, double width) {
        if (location != null) {
            // bind the texture
            bindTexture(location);
        }

        double scaledWidth = 1.0 / width;
        double scaledHeight = 1.0 / height;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.enableTexture2D();

        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0).tex(u * scaledWidth, (v + height) * scaledHeight).endVertex();
        buffer.pos(x + width, y + height, 0.0).tex((u + width) * scaledWidth, (v + height) * scaledHeight).endVertex();
        buffer.pos(x + width, y, 0.0).tex((u + width) * scaledWidth, v * scaledHeight).endVertex();
        buffer.pos(x, y, 0.0).tex(u * scaledWidth, v * scaledHeight).endVertex();
        tessellator.draw();
    }
}
