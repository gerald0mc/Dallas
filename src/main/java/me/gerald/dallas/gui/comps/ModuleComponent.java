package me.gerald.dallas.gui.comps;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.gui.api.AbstractContainer;
import me.gerald.dallas.gui.comps.settingcomps.*;
import me.gerald.dallas.mod.Module;
import me.gerald.dallas.mod.mods.client.GUI;
import me.gerald.dallas.setting.Setting;
import me.gerald.dallas.setting.settings.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends AbstractContainer {
    public Module module;
    public Module.Category category;
    public boolean open = false;
    public List<AbstractContainer> settingComponents = new ArrayList<>();

    public ModuleComponent(Module module, Module.Category category, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.module = module;
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        settingComponents.add(new BindComponent(module, x, y, 110, height));
        for(Setting setting : module.getSettings()) {
            if(setting instanceof BooleanSetting)
                settingComponents.add(new BooleanComponent((BooleanSetting) setting, x, y, 110, height));
            else if(setting instanceof NumberSetting)
                settingComponents.add(new NumberComponent((NumberSetting) setting, x, y, 110, height));
            else if(setting instanceof ModeSetting)
                settingComponents.add(new ModeComponent((ModeSetting) setting, x, y, 110, height));
            else if(setting instanceof StringSetting)
                settingComponents.add(new StringComponent((StringSetting) setting, x, y, 110, height));
            else if(setting instanceof ColorSetting)
                settingComponents.add(new ColorComponent((ColorSetting) setting, x, y, 110, height));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yOffset = 0;
        Gui.drawRect(x, y, x + width, y + height, module.isEnabled() ? new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).red.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).green.getValue() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).blue.getValue() / 255f).getRGB() : new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName(), x + 2, y + 2f, -1);
        if(isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = module.getDescription();
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(module.getDescription()) + 3;
        }
        if(open) {
            for(AbstractContainer component : settingComponents) {
                component.x = x + width;
                component.y = y + yOffset;
                yOffset += component.getHeight();
                component.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for(AbstractContainer component : settingComponents) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
        if(isInside(mouseX, mouseY)) {
            if(mouseButton == 0) {
                module.toggle();
            }else if(mouseButton == 1) {
                for(CategoryComponent categoryComponent : Yeehaw.INSTANCE.clickGUI.categories) {
                    for(ModuleComponent moduleComponent : categoryComponent.modules) {
                        if(moduleComponent.open && moduleComponent.module != module && moduleComponent.module.getCategory() == module.getCategory()) moduleComponent.open = false;
                    }
                }
                open = !open;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(open) {
            for(AbstractContainer component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if(open) {
            for(AbstractContainer component : settingComponents) {
                component.keyTyped(keyChar, key);
            }
        }
    }

    @Override
    public int getHeight() {
        return height;
    }
}
