package me.gerald.dallas.features.gui.comps;

import me.gerald.dallas.features.gui.api.AbstractContainer;
import me.gerald.dallas.features.gui.api.HUDContainer;
import me.gerald.dallas.features.gui.clickgui.ClickGUI;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockSelectionComponent extends HUDContainer {
    public BlockSelectionComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public List<Thing> blockPosList = new ArrayList<>();
    public boolean listDone = false;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateDragPosition(mouseX, mouseY);
        if(!blockPosList.isEmpty() && blockPosList.get(0).x != x)
            blockPosList.clear();
        Gui.drawRect(x, y, x + (18 * 12), y + height + 18, new Color(0, 0, 0, 170).getRGB());
        int xOffset = x;
        int yOffset = 0;
        for(Block block : Block.REGISTRY) {
            if(block.equals(Blocks.AIR)) continue;
            if(block instanceof BlockAir) continue;
            ItemStack stack = new ItemStack(block);
            if(stack.getItem() == Items.AIR) continue;
            if(stack.getItem() instanceof ItemAir) continue;
            if (xOffset == x + (18 * 12)) {
                yOffset += 18;
                xOffset = x;
            }
            if(blockPosList.size() != 202)
                blockPosList.add(new Thing(block, xOffset, y + yOffset));
            for(Thing thing : blockPosList) {
                if(thing.getBlock() == block) {
                    Gui.drawRect(xOffset, y + yOffset, xOffset + 18, y + yOffset + 17, thing.clicked ? ClickGUI.clientColor.getRGB() : new Color(0, 0, 0, 0).getRGB());
                    break;
                }
            }
            RenderUtil.renderBorder(xOffset, y + yOffset, xOffset + 18, y + yOffset + 17, 1, new Color(0, 0, 0, 255));
            RenderUtil.renderItem(new ItemStack(block), "", xOffset + 1, y + yOffset);
            xOffset += 18;
        }
        this.height = yOffset;
        if(!listDone) listDone = true;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isInside(mouseX, mouseY, x, y, (18 * 12), height + 18)) {
            if (mouseButton == 0)
                beginDragging(mouseX, mouseY);
        }
        for(Thing thing : blockPosList) {
            if (isInside(mouseX, mouseY, thing.x, thing.y, 18, 17)) {
                if(mouseButton == 0) {
                    thing.clicked = !thing.clicked;
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) { stopDragging(); }

    @Override
    public void keyTyped(char keyChar, int key) throws IOException, UnsupportedFlavorException { }

    @Override
    public int getHeight() {
        return height;
    }

    public static class Thing {
        private final Block block;
        private final int x;
        private final int y;
        private boolean clicked = false;

        public Thing(Block block, int x, int y) {
            this.block = block;
            this.x = x;
            this.y = y;
        }

        public Block getBlock() {
            return block;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
