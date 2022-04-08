package me.gerald.dallas.gui.clickgui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.mods.client.GUI;
import me.gerald.dallas.setting.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class BooleanComponent extends AbstractContainer {
    public BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, setting.getValue() ? new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB() : new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName() + " " + ChatFormatting.GRAY + (setting.getValue() ? "True" : "False"), x + 2, y + 2f, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A boolean setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A boolean setting called (" + setting.getName() + ").") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                setting.cycle();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void keyTyped(char keyChar, int key) {}

    @Override
    public int getHeight() {
        return height;
    }
}
