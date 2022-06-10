package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberComponent extends SettingComponent {
    public NumberSetting setting;
    public float sliderWidth;
    public boolean dragging = false;

    boolean changeColor = true;
    Color sliderColor = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);

    public NumberComponent(NumberSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static float roundToPlace(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateSliderLogic(mouseX);
        if (changeColor) {
            sliderColor = ClickGUI.clientColor;
        }
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Gui.drawRect(x, y, x + (int) sliderWidth, y + height, sliderColor.getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(trimValue("", setting.getName(), String.valueOf(setting.getValue()), 6), x + 4, y + 3, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + String.valueOf(setting.getValue()), x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth(String.valueOf(setting.getValue())) - 4, y + 3, -1);
        RenderUtil.renderBorderToggle(x, y, x + width, y + height, 1, new Color(0, 0, 0, 255), false, true, true, last);
        if (isInside(mouseX, mouseY)) {
            if (needsHover) {
                Gui.drawRect(mouseX + 5, mouseY - 5 - Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT, mouseX + 8 + Minecraft.getMinecraft().fontRenderer.getStringWidth(setting.getName()), mouseY - 5, new Color(0, 0, 0, 255).getRGB());
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), mouseX + 7, mouseY - 13, -1);
            }
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = setting.getDescription();
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(setting.getDescription()) + 8;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = !dragging;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (dragging)
            dragging = false;
    }

    @Override
    public void keyTyped(char keyChar, int key) {
    }

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
}
