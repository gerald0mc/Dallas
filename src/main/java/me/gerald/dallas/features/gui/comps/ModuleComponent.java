package me.gerald.dallas.features.gui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.features.gui.api.SettingComponent;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.features.gui.comps.settingcomps.*;
import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModuleComponent extends AbstractContainer {
    public Module module;
    public Module.Category category;
    public boolean open = false;
    public boolean lastModule = false;
    public List<SettingComponent> settingComponents = new ArrayList<>();
    public BindComponent bindComponent;

    public ModuleComponent(Module module, Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bindComponent = new BindComponent(module, x, y, 110, height);
        Iterator<Setting> iterator = module.getSettings().iterator();
        while (iterator.hasNext()) {
            Setting element = iterator.next();
            if (element instanceof BooleanSetting)
                settingComponents.add(new BooleanComponent((BooleanSetting) element, x, y, 110, height));
            else if (element instanceof NumberSetting)
                settingComponents.add(new NumberComponent((NumberSetting) element, x, y, 110, height));
            else if (element instanceof ModeSetting)
                settingComponents.add(new ModeComponent((ModeSetting) element, x, y, 110, height));
            else if (element instanceof StringSetting)
                settingComponents.add(new StringComponent((StringSetting) element, x, y, 110, height));
            else if (element instanceof ColorSetting)
                settingComponents.add(new ColorComponent((ColorSetting) element, x, y, 110, height));
            if (!iterator.hasNext()) {
                settingComponents.get(settingComponents.size() - 1).last = true;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yOffset = 0;
        float alignment = 0;
        switch (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).moduleAlignment.getMode()) {
            case "Middle":
                alignment = x + width / 2f - (Minecraft.getMinecraft().fontRenderer.getStringWidth((open ? "> " : "") + module.getName()) / 2f);
                break;
            case "Left":
                alignment = x + 2f;
                break;
            case "Right":
                alignment = x + width - Minecraft.getMinecraft().fontRenderer.getStringWidth((open ? "> " : "") + module.getName()) - 2;
                break;
        }
        Gui.drawRect(x, y, x + width, y + height, module.isEnabled() ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((open ? "> " : "") + module.getName(), alignment, y + 2f, -1);
        //left line
        Gui.drawRect(x, y, x + 1, y + height, new Color(0, 0, 0, 255).getRGB());
        //right line
        Gui.drawRect(x + width - 1, y, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        if (lastModule) {
            //bottom line
            Gui.drawRect(x, y + height - 1, x + width, y + height, new Color(0, 0, 0, 255).getRGB());
        }
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = module.getDescription();
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(module.getDescription()) + 3;
        }
        if (open) {
            bindComponent.x = x + width;
            bindComponent.y = y + yOffset;
            yOffset += bindComponent.getHeight();
            if (settingComponents.size() == 0) {
                bindComponent.onlySetting = true;
            }
            bindComponent.drawScreen(mouseX, mouseY, partialTicks);
            if (settingComponents.isEmpty()) return;
            for (SettingComponent component : settingComponents) {
                if (!component.isVisible()) continue;
                component.x = x + width;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            bindComponent.mouseClicked(mouseX, mouseY, mouseButton);
            for (AbstractContainer component : settingComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                module.toggle();
            } else if (mouseButton == 1) {
                for (CategoryComponent categoryComponent : Yeehaw.INSTANCE.clickGUI.categories) {
                    for (ModuleComponent moduleComponent : categoryComponent.modules) {
                        if (moduleComponent.open && moduleComponent.module != module && moduleComponent.module.getCategory() == module.getCategory())
                            moduleComponent.open = false;
                    }
                }
                open = !open;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (open) {
            bindComponent.mouseReleased(mouseX, mouseY, mouseButton);
            for (AbstractContainer component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if (open) {
            bindComponent.keyTyped(keyChar, key);
            for (AbstractContainer component : settingComponents) {
                component.keyTyped(keyChar, key);
            }
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
