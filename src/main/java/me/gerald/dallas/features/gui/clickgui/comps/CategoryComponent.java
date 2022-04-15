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
        if (Yeehaw.INSTANCE.moduleManager.getCategory(category) != null) {
            Iterator<Module> iterator = Yeehaw.INSTANCE.moduleManager.getCategory(category).iterator();
            while (iterator.hasNext()) {
                Module element = iterator.next();
                this.modules.add(new ModuleComponent(element, category, x, y, width, height));
                if (!iterator.hasNext()) {
                    this.modules.get(this.modules.size() - 1).lastModule = true;
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.dragging) {
            this.x = this.dragX + mouseX;
            this.y = this.dragY + mouseY;
        }
        float alignment = 0;
        String text = null;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryAllignment.getMode()) {
            case "Middle":
                alignment = this.x + this.width / 2f - (Minecraft.getMinecraft().fontRenderer.getStringWidth((this.open ? "> " : "V ") + this.category.toString()) / 2f);
                text = (this.open ? "> " : "V ") + this.category.toString();
                break;
            case "Left":
                alignment = this.x + 2f;
                text = (this.open ? "> " : "V ") + this.category.toString();
                break;
            case "Right":
                alignment = this.x + this.width - Minecraft.getMinecraft().fontRenderer.getStringWidth(this.category.toString() + (this.open ? " >" : " V")) - 2;
                text = this.category.toString() + (this.open ? " >" : " V");
                break;
        }
        Gui.drawRect(this.x - 2, this.y, this.x + this.width + 2, this.y + this.height, new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB());
        //borders
        //top lines
        Gui.drawRect(this.x - 2, this.y, this.x + this.width + 2, this.y + 1, new Color(0, 0, 0, 255).getRGB());
        //left line
        Gui.drawRect(this.x - 2, this.y, this.x - 1, this.y + this.height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(this.x + this.width + 1, this.y, this.x + this.width + 2, this.y + this.height, new Color(0, 0, 0, 255).getRGB());
        //bottom line
        Gui.drawRect(this.x - 2, this.y + this.height - 1, this.x + this.width + 2, this.y + this.height, new Color(0, 0, 0, 255).getRGB());

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, alignment, this.y + 2, -1);
        if (this.isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = this.category.toString() + " category.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(this.category.toString() + " category.") + 3;
        }
        int yOffset = this.height;
        for (ModuleComponent component : this.modules) {
            if (!this.open) return;
            component.x = this.x;
            component.y = this.y + yOffset;
            yOffset += component.getHeight();
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.dragging = !this.dragging;
                this.dragX = this.x - mouseX;
                this.dragY = this.y - mouseY;
            } else if (mouseButton == 1) {
                this.open = !this.open;
            }
        }
        for (ModuleComponent component : this.modules) {
            if (!this.open) return;
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.dragging) {
            this.dragging = false;
        }

        for (ModuleComponent component : this.modules) {
            if (!this.open) return;
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        for (ModuleComponent component : this.modules) {
            if (!this.open) return;
            component.keyTyped(keyChar, key);
        }
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
