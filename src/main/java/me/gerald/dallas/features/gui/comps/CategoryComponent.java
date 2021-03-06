package me.gerald.dallas.features.gui.comps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.DragComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryComponent extends DragComponent {
    public Module.Category category;
    public List<ModuleComponent> modules = new ArrayList<>();
    boolean open = true;

    public CategoryComponent(Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        Yeehaw.INSTANCE.moduleManager.getCategory(category).forEach(
                module -> modules.add(new ModuleComponent(module, category, x, y, width, height - 2))
        );
        if (modules.size() != 0)
            modules.get(modules.size() - 1).lastModule = true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        float alignment = 0;
        String text = "";
        switch (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryAlignment.getMode()) {
            case "Middle":
                alignment = x + width / 2f - (Minecraft.getMinecraft().fontRenderer.getStringWidth(category.toString() + (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleCount.getValue() ? " [" + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + "]" : "")) / 2f);
                text = category.toString() + (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleCount.getValue() ? " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + ChatFormatting.GRAY + "]" : "");
                break;
            case "Left":
                alignment = Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? x + Minecraft.getMinecraft().fontRenderer.getStringWidth(open ? "> " : "V ") : x + 2f + Minecraft.getMinecraft().fontRenderer.getStringWidth(open ? "> " : "V ");
                text = category.toString() + (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleCount.getValue() ? " " + ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + ChatFormatting.GRAY + "]" : "");
                break;
            case "Right":
                alignment = x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth((Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleCount.getValue() ? "[" + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + "] " : "") + category.toString()) - (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? 1 : 2);
                text = (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleCount.getValue() ? ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + Yeehaw.INSTANCE.moduleManager.getAmountPerCat(category) + ChatFormatting.GRAY + "] " + ChatFormatting.RESET : "") + category.toString();
                break;
        }
        Gui.drawRect(x - (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? 2 : 0), y, x + width + (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? 2 : 0), y + height, ClickGUI.clientColor.getRGB());
        if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).border.getValue())
            RenderUtil.renderBorder(x - (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? 2 : 0), y, x + width + (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).categoryOverhang.getValue() ? 2 : 0), y + height, 1, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).borderColor.getColor());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(open ? "> " : "V ", x + 2, y + 4, -1);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, alignment, y + 4, -1);
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = category.toString() + " category.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(category.toString() + " category.") + 8;
        }
        int yOffset = height;
        if (open) for (ModuleComponent component : modules) {
            component.x = x;
            component.y = y + yOffset;
            yOffset += component.getHeight();
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                beginDragging(mouseX, mouseY);
            } else if (mouseButton == 1) {
                open = !open;
            }
        }
        if (open) for (ModuleComponent component : modules) {
            if (component.isInside(mouseX, mouseY)) {
                for (Module.Category category : Module.Category.values()) {
                    if (component.module.getCategory() == category) {
                        for (CategoryComponent categoryComponent : Yeehaw.INSTANCE.clickGUI.categories) {
                            if (categoryComponent.category == category) {
                                Yeehaw.INSTANCE.clickGUI.priorityComponent = categoryComponent;
                            }
                        }
                    }
                }
            }
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isDragging()) {
            stopDragging();
        }

        if (open) for (ModuleComponent component : modules) {
            component.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if (open) for (ModuleComponent component : modules) {
            component.keyTyped(keyChar, key);
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
