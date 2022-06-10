package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.setting.settings.ModeSetting;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class ModeComponent extends SettingComponent {
    public ModeSetting setting;

    public ModeComponent(ModeSetting setting, int x, int y, int width, int height) {
        super(setting, x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(trimValue("", setting.getName(), setting.getMode(), 6), x + 4, y + 3, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ChatFormatting.GRAY + setting.getMode(), x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth(setting.getMode()) - 4, y + 3, -1);
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
            if (mouseButton == 0)
                setting.decrease();
            else if (mouseButton == 1)
                setting.increase();
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
