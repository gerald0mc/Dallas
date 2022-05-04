package me.gerald.dallas.features.gui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.features.modules.client.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class BindComponent extends AbstractContainer {
    public Module module;
    public boolean listening = false;
    public boolean onlySetting = false;

    public BindComponent(Module module, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, listening ? new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f).getRGB() : new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? ChatFormatting.GRAY + "Listening..." : "Bind " + ChatFormatting.GRAY + Keyboard.getKeyName(module.getKeybind()), x + 2, y + 2f, -1);
        //borders
        //top lines
        Gui.drawRect(x, y, x + width, y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(x, y, x + 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(x + width - 1, y, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        if (onlySetting) {
            //bottom line
            Gui.drawRect(x, y + height - 1, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        }
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A bind setting.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A bind setting.") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                listening = !listening;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) {
        if (listening) {
            module.setKeybind(key);
            listening = false;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
