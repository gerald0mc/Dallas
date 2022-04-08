package me.gerald.dallas.gui.clickgui.comps.settingcomps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class BindComponent extends AbstractContainer {
    public Module module;
    public boolean listening = false;

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
        Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? ChatFormatting.GRAY + "Listening..." : "Bind " + ChatFormatting.GRAY + Keyboard.getKeyName(module.getKeybind()), x + 2, y + 2f, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "A bind setting.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("A bind setting.") + 3;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0)
                listening = !listening;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    @Override
    public void keyTyped(char keyChar, int key) {
        if(listening) {
            module.setKeybind(key);
            listening = false;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
