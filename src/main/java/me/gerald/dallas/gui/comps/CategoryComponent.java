package me.gerald.dallas.gui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.mod.mods.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryComponent extends AbstractContainer {
    public Module.Category category;
    public List<ModuleComponent> modules = new ArrayList<>();
    int yOffset = height;
    boolean open = true;

    public CategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(Yeehaw.INSTANCE.moduleManager.getCategory(category) != null) {
            for (Module module : Yeehaw.INSTANCE.moduleManager.getCategory(category)) {
                modules.add(new ModuleComponent(module, category, x, y + yOffset, width, height));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((open ? "> " : "V ") + category.toString(), x + 2, y + 2, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = category.toString() + " category.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(category.toString() + " category.") + 3;
        }
        int yOffset = height;
        for(ModuleComponent component : modules) {
            if(!open) return;
            component.x = this.x;
            component.y = this.y + yOffset;
            yOffset += component.getHeight();
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 1) {
                open = !open;
            }
        }
        for(ModuleComponent component : modules) {
            if(!open) return;
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for(ModuleComponent component : modules) {
            if(!open) return;
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        for(ModuleComponent component : modules) {
            if(!open) return;
            component.keyTyped(keyChar, key);
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
