package me.gerald.dallas.features.modules.hud.speed;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.MotionUpdateEvent;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SpeedComponent extends HUDContainer {
    public SpeedComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private double motionSpeed = 0.0;

    @SubscribeEvent
    public void onMotionUpdate(MotionUpdateEvent event) {
        double x = mc.player.posX - mc.player.prevPosX;
        double z = mc.player.posZ - mc.player.prevPosZ;

        motionSpeed = Math.sqrt(x * x + z * z);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);

        Color color;
        if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
            color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
        } else {
            color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
        }

        width = Minecraft.getMinecraft().fontRenderer.getStringWidth("Speed: " + getSpeed());
        height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Speed" + ChatFormatting.GRAY + ": " + ChatFormatting.WHITE + getSpeed(), x, y, color.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {

    }

    @Override
    public int getHeight() {
        return height;
    }

    private String getSpeed() {
        double speed = motionSpeed * 71.2729367892;
        return String.format("%.2f", speed);
    }
}
