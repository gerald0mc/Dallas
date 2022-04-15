package me.gerald.dallas.features.gui.clickgui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.client.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CategoryComponent extends AbstractContainer {
    public Module.Category category;
    public List<ModuleComponent> modules = new ArrayList<>();
    boolean open = true;

    private boolean dragging = false;
    private int dragX, dragY;

    public CategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if(Yeehaw.INSTANCE.moduleManager.getCategory(category) != null) {
            Iterator<Module> iterator = Yeehaw.INSTANCE.moduleManager.getCategory(category).iterator();
            while(iterator.hasNext()) {
                Module element = iterator.next();
                modules.add(new ModuleComponent(element, category, x, y, width, height));
                if(!iterator.hasNext()) {
                    modules.get(modules.size() - 1).lastModule = true;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (dragging) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }
        float alignment = 0;
        String text = null;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryAllignment.getMode()) {
            case "Middle":
                alignment = x + width / 2f - (Minecraft.getMinecraft().fontRenderer.getStringWidth((open ? "> " : "V ") + category.toString()) / 2f);
                text = (open ? "> " : "V ") + category.toString();
                break;
            case "Left":
                alignment = x + 2f;
                text = (open ? "> " : "V ") + category.toString();
                break;
            case "Right":
                alignment = x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth(category.toString() + (open ? " >" : " V")) - 2;
                text = category.toString() + (open ? " >" : " V");
                break;
        }
        Gui.drawRect(x - 2, y, x + width + 2, y + height, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        //borders
        //top lines
        Gui.drawRect(x - 2, y, x + width + 2, y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(x - 2, y, x -1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(x + width + 1, y, x + width + 2, y + height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(x - 2, y + height - 1, x + width + 2, y + height, new Color(0, 0, 0, 255).getRGB());

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, alignment, y + 2, -1);
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
            if (mouseButton == 0) {
                dragging = !dragging;
                dragX = x - mouseX;
                dragY = y - mouseY;
            } else if(mouseButton == 1) {
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
        if (mouseButton == 0 && dragging) {
            dragging = false;
        }

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
