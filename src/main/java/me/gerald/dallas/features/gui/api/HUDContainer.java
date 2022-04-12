package me.gerald.dallas.features.gui.api;

import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class HUDContainer extends DragComponent {
    public HUDContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Minecraft.getMinecraft().currentScreen instanceof ClickGUI) {
            Gui.drawRect(x, y, x + width, y + height, isInside(mouseX, mouseY) ? 0x50ffffff : 0x90000000);
        }
    }
}
