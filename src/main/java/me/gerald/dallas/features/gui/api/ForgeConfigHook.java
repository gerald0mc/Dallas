package me.gerald.dallas.features.gui.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import me.gerald.dallas.Yeehaw;

import java.util.Set;

/**
 * @author bush
 * @since 4/14/2022
 */
public class ForgeConfigHook implements IModGuiFactory {

    @Override
    public void initialize(Minecraft mc) {}

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return Yeehaw.INSTANCE.clickGUI;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
