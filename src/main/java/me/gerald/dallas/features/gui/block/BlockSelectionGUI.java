package me.gerald.dallas.features.gui.block;

import me.gerald.dallas.features.gui.comps.BlockSelectionComponent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class BlockSelectionGUI extends GuiScreen {
    public BlockSelectionComponent selectionComponent;

    public BlockSelectionGUI() {
        MinecraftForge.EVENT_BUS.register(this);
        selectionComponent = new BlockSelectionComponent(25, 25, 0, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        selectionComponent.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        selectionComponent.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectionComponent.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
