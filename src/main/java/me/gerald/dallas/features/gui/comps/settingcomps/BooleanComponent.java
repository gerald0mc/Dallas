package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.setting.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComponent extends SettingComponent {
    public BooleanSetting setting;
    private final boolean showValue;

    public BooleanComponent(BooleanSetting setting, boolean showValue, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        this.showValue = showValue;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, setting.getValue() ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + (showValue ? " " + ChatFormatting.GRAY + (setting.getValue() ? "True" : "False") : ""), x + 2, y + 2f, -1);
        //left line
        Gui.drawRect(x, y, x + 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(x + width - 1, y, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        if (last) {
            //bottom line
            Gui.drawRect(x, y + height - 1, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        }
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A boolean setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A boolean setting called (" + setting.getName() + ").") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                setting.cycle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) {
    }

    @Override
    public int getHeight() {
        return height;
    }
}
