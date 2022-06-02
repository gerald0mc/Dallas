package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class BooleanComponent extends SettingComponent {
    private final boolean showValue;
    public BooleanSetting setting;

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
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(trimValue("", setting.getName(), setting.getValue() ? "True" : "False", 6), x + 4, y + 3, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(showValue ? ChatFormatting.GRAY + (setting.getValue() ? "True" : "False") : "", x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth(setting.getValue() ? "True" : "False") - 4, y + 3, -1);
        RenderUtil.renderBorderToggle(x, y, x + width, y + height, 1, new Color(0, 0, 0, 255), false, true, true, last);
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A boolean setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A boolean setting called (" + setting.getName() + ").") + 8;
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
