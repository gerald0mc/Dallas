package me.gerald.dallas.features.gui.comps;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StringEntryComponent extends AbstractContainer {
    public String defaultText;
    public String entryString = "";
    public boolean listening;

    public List<ModuleComponent> searchModules;
//    public List<SettingComponent> settingComponents;

    public StringEntryComponent(String defaultText, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.defaultText = defaultText;
        searchModules = new ArrayList<>();
//        settingComponents = new ArrayList<>();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(x, y, x + width, y + height + 1, new Color(0, 0, 0, 125).getRGB());
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(listening ? entryString : ChatFormatting.GRAY + defaultText, x + 4, y + 3, -1);
        RenderUtil.renderBorder(x, y, x + width, y + height + 1, 1, new Color(0, 0, 0, 255));
        if (isInside(mouseX, mouseY)) {
            Yeehaw.INSTANCE.clickGUI.descriptionBox.text = "Search component.";
            Yeehaw.INSTANCE.clickGUI.descriptionBox.width = Minecraft.getMinecraft().fontRenderer.getStringWidth("Search component.") + 8;
        }
        int yOffset = height + 1;
        for (ModuleComponent component : searchModules) {
            component.x = x;
            component.y = y + yOffset;
            yOffset += component.getHeight();
            component.drawScreen(mouseX, mouseY, partialTicks);
        }
//        for(SettingComponent component : settingComponents) {
//            component.x = x;
//            component.y = y + yOffset;
//            yOffset += component.getHeight();
//            component.drawScreen(mouseX, mouseY, partialTicks);
//        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                listening = !listening;
            if (!listening)
                searchModules.clear();
        }
        for (ModuleComponent component : searchModules) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
        if (key == Keyboard.KEY_BACK) {
//            settingComponents.clear();
            searchModules.clear();
            entryString = removeLastLetter(entryString);
            for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if (StringUtils.containsIgnoreCase(module.getName(), entryString)) {
                    searchModules.add(new ModuleComponent(module, module.getCategory(), x, y, width, height));
                }
//                for(Setting setting : modules.getSettings()) {
//                    if(setting.getName().contains(entryString)) {
//                        if (setting instanceof BooleanSetting)
//                            settingComponents.add(new BooleanComponent((BooleanSetting) setting, x, y, 100, height));
//                        else if (setting instanceof NumberSetting)
//                            settingComponents.add(new NumberComponent((NumberSetting) setting, x, y, 100, height));
//                        else if (setting instanceof ModeSetting)
//                            settingComponents.add(new ModeComponent((ModeSetting) setting, x, y, 100, height));
//                        else if (setting instanceof StringSetting)
//                            settingComponents.add(new StringComponent((StringSetting) setting, x, y, 100, height));
//                        else if (setting instanceof ColorSetting)
//                            settingComponents.add(new ColorComponent((ColorSetting) setting, x, y, 100, height));
//                    }
//                }
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
//            settingComponents.clear();
            searchModules.clear();
            entryString += keyChar;
            for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
                if (StringUtils.containsIgnoreCase(module.getName(), entryString)) {
                    searchModules.add(new ModuleComponent(module, module.getCategory(), x, y, width, height));
                }
//                for(Setting setting : modules.getSettings()) {
//                    if(setting.getName().contains(entryString)) {
//                        if (setting instanceof BooleanSetting)
//                            settingComponents.add(new BooleanComponent((BooleanSetting) setting, x, y, 100, height));
//                        else if (setting instanceof NumberSetting)
//                            settingComponents.add(new NumberComponent((NumberSetting) setting, x, y, 100, height));
//                        else if (setting instanceof ModeSetting)
//                            settingComponents.add(new ModeComponent((ModeSetting) setting, x, y, 100, height));
//                        else if (setting instanceof StringSetting)
//                            settingComponents.add(new StringComponent((StringSetting) setting, x, y, 100, height));
//                        else if (setting instanceof ColorSetting)
//                            settingComponents.add(new ColorComponent((ColorSetting) setting, x, y, 100, height));
//                    }
//                }
            }
        }
    }

    public String removeLastLetter(String string) {
        String out = "";
        if (string != null && string.length() > 0) {
            out = string.substring(0, string.length() - 1);
        }
        return out;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
