package me.gerald.dallas.features.modules.hud.arraylist;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.modules.client.GUI;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ArrayListComponent extends HUDContainer {
    public HashMap<Module, Color> randomColorMap = new LinkedHashMap<>();
    Random random = new Random();
    public ArrayListComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        List<String> moduleNames = new ArrayList<>();
        Color color = null;
        if (Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).colorMode.getMode().equals("Default")) {
            if (Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbow.getValue()) {
                color = RenderUtil.genRainbow((int) Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).rainbowSpeed.getValue());
            } else {
                color = new Color(Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getR() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getG() / 255f, Yeehaw.INSTANCE.moduleManager.getModule(GUI.class).color.getB() / 255f);
            }
        }
        int yOffset = 0;
        for (Module module : Yeehaw.INSTANCE.moduleManager.getModules()) {
            if (!randomColorMap.containsKey(module))
                randomColorMap.put(module, new Color(random.nextInt(255) / 255f, random.nextInt(255) / 255f, random.nextInt(255) / 255f));
            if (Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).colorMode.getMode().equals("Category")) {
                color = module.getCategoryColor();
            } else if (Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).colorMode.getMode().equals("Random")) {
                color = randomColorMap.get(module);
            }
            if (module.getCategory() == Module.Category.HUD && Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).skipHUD.getValue())
                continue;
            if (module.isEnabled()) {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(module.getName() + (module.getMetaData().equals("") ? "" : ChatFormatting.GRAY + "[" + ChatFormatting.WHITE + module.getMetaData() + ChatFormatting.GRAY + "]"), x, y + yOffset, color.getRGB());
                yOffset += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + Yeehaw.INSTANCE.moduleManager.getModule(me.gerald.dallas.features.modules.hud.arraylist.ArrayList.class).spacing.getValue();
                moduleNames.add(module.getName() + "[" + module.getMetaData() + "]");
            } else {
                moduleNames.remove(module.getName());
            }
        }
        width = Minecraft.getMinecraft().fontRenderer.getStringWidth(moduleNames.size() == 0 ? "Whole lotta cock" : getLongestWord(moduleNames));
        height = yOffset == 0 ? Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT : yOffset;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
