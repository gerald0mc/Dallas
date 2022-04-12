package me.gerald.dallas.gui;

import me.gerald.dallas.gui.clickgui.comps.CategoryComponent;
import me.gerald.dallas.gui.clickgui.comps.TextComponent;
import me.gerald.dallas.mod.Module;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends GuiScreen {
    public TextComponent descriptionBox = new TextComponent("Description's will appear here.", 25, 35, 100, 11);
    public List<CategoryComponent> categories = new ArrayList<>();

    public ClickGUI() {
        int xOffset = 25;
        for(Module.Category category : Module.Category.values()) {
            categories.add(new CategoryComponent(category, xOffset, 50, 100, 11));
            xOffset += 210;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(CategoryComponent component : categories) {
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
        descriptionBox.drawScreen(mouseX, mouseY, partialTicks);
        if(descriptionBox.width != 156)
            descriptionBox.width = 156;
        if(!descriptionBox.text.equalsIgnoreCase("Description's will appear here."))
            descriptionBox.text = "Description's will appear here.";
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(CategoryComponent component : categories) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryComponent component : categories) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for(CategoryComponent component : categories) {
            try {
                component.keyTyped(typedChar, keyCode);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
