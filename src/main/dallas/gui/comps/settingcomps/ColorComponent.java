package me.gerald.dallas.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.gui.comps.ClickComponent;
import me.gerald.dallas.setting.settings.ColorSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColorComponent extends AbstractContainer {
    public ColorSetting setting;
    public boolean open = false;

    public List<NumberComponent> rgbaSliders;

    public ColorComponent(ColorSetting setting, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        rgbaSliders = new ArrayList<>();
        rgbaSliders.add(new NumberComponent(new NumberSetting("Red", setting.getR(), 0, 255), x, y, width, height));
        rgbaSliders.add(new NumberComponent(new NumberSetting("Green", setting.getG(), 0, 255), x, y, width, height));
        rgbaSliders.add(new NumberComponent(new NumberSetting("Blue", setting.getB(), 0, 255), x, y, width, height));
        rgbaSliders.add(new NumberComponent(new NumberSetting("Alpha", setting.getA(), 0, 255), x, y, width, height));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(setting.getName(), x + 2, y + 2f, -1);
        Gui.drawRect(x + width - 12, y + 1, x + width - 2, y + height - 1, new Color(setting.getR(), setting.getG(), setting.getB(), 255).getRGB());
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A color setting called (" + setting.getName() + ").";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A color setting called (" + setting.getName() + ").") + 3;
        }
        if(open) {
            int yOffset = height;
            for (AbstractContainer component : rgbaSliders) {
                component.x = x;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
            rgbaSliders.get(0).sliderColor = new Color(255, 0, 0);
            rgbaSliders.get(1).sliderColor = new Color(0, 255, 0);
            rgbaSliders.get(2).sliderColor = new Color(0, 0, 255);
            setting.setR((int) rgbaSliders.get(0).setting.getValue());
            setting.setG((int) rgbaSliders.get(1).setting.getValue());
            setting.setB((int) rgbaSliders.get(2).setting.getValue());
            setting.setA((int) rgbaSliders.get(3).setting.getValue());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 1) {
                open = !open;
            }
        }
        if(open) {
            for(AbstractContainer container : rgbaSliders) {
                container.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for (AbstractContainer container : rgbaSliders) {
                container.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException { }

    @Override
    public int getHeight() {
        if(open) {
            int h = height;
            for(AbstractContainer component : rgbaSliders) {
                h += component.getHeight();
            }
            return h;
        }
        return height;
    }
}
