package me.gerald.dallas.gui.clickgui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.mods.client.GUI;
import me.gerald.dallas.setting.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends AbstractContainer {
    public NumberSetting setting;
    public float sliderWidth;
    public boolean dragging = false;
    public Color sliderColor = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f);

    public NumberComponent(NumberSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateSliderLogic(mouseX);
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Gui.drawRect(x, y, x + (int) sliderWidth, y + height, sliderColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " " + ChatFormatting.GRAY + setting.getValue(), x + 2, y + 2f, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A number setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A number setting called (" + setting.getName() + ").") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                dragging = !dragging;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(dragging)
            dragging = false;
    }

    @Override
    public void keyTyped(char keyChar, int key) {}

    @Override
    public int getHeight() {
        return height;
    }

    protected void updateSliderLogic(int mouseX) {
        float diff = Math.min(width, Math.max(0, mouseX - x));
        float min = setting.getMin();
        float max = setting.getMax();
        sliderWidth = width * (setting.getValue() - min) / (max - min);
        if (dragging) {
            if (diff == 0) {
                setting.setValue(setting.getMin());
            } else {
                float value = roundToPlace(diff / width * (max - min) + min, 1);
                setting.setValue(value);
            }
        }
    }

    public static float roundToPlace(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
