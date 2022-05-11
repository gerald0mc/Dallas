package me.gerald.dallas.features.modules.hud.arraylist;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.gui.comps.BlockSelectionComponent;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class ArrayListComponent extends HUDContainer {
    public ArrayListComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    public BlockSelectionComponent selectionComponent = new BlockSelectionComponent(x, y, 0, 0);

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        List<String> moduleNames = new ArrayList<>();
        Color color;
        if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
            color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
        } else {
            color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
        }
        int yOffset = 0;
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            if (module.getCategory() == Module.Category.HUD && Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).skipHUD.getValue())
                continue;
            if (module.isEnabled()) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName() + module.getMetaData(), x, y + yOffset, color.getRGB());
                yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).spacing.getValue();
                moduleNames.add(module.getName() + module.getMetaData());
            } else {
                moduleNames.remove(module.getName());
            }
        }
        selectionComponent.x = x;
        selectionComponent.y = y;
        selectionComponent.drawScreen(mouseX, mouseY, partialTicks);
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(moduleNames.size() == 0 ? "Whole lotta cock" : getLongestWord(moduleNames));
        height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + yOffset;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        selectionComponent.mouseClicked(mouseX, mouseY, mouseButton);
        if (isInside(mouseX, mouseY)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        stopDragging();
    }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException {
    }

    @Override
    public int getHeight() {
        return height;
    }

    public String getLongestWord(List<String> strings) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (String s : strings)
            hashMap.put(s, Minecraft.getMinecraft().fontRenderer.getStringWidth(s));
        return Collections.max(hashMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }
}
