package me.gerald.dallas.features.modules.render;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.utils.ProjectionUtil;
import me.gerald.dallas.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShulkerNametags extends Module {
    public BooleanSetting counts = new BooleanSetting("Counts", false);

    public ShulkerNametags() {
        super("ShulkerNametags", Category.RENDER, "Renders the shulker as a nametag.");
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityItem) {
                EntityItem item = (EntityItem) e;
                if(item.getItem().getItem() instanceof ItemShulkerBox) {
                    Vec3d projection = ProjectionUtil.toScaledScreenPos(item.getPositionVector());
                    renderShulkerToolTip(item.getItem(), (int) projection.x - 80, (int) projection.y - 80);
                }
            }
        }
    }

    public void renderShulkerToolTip(ItemStack stack, int x, int y) {
        NBTTagCompound blockEntityTag;
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10) && (blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag")).hasKey("Items", 9)) {
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            NonNullList<ItemStack> nonNullList = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(blockEntityTag, nonNullList);
            for (int i = 0; i < nonNullList.size(); ++i) {
                int iX = x + i % 9 * 18 + 8;
                int iY = y + i / 9 * 18 + 18;
                ItemStack itemStack = nonNullList.get(i);
                RenderUtil.renderItem(itemStack, counts.getValue() ? itemStack.getCount() == 1 ? "" : String.valueOf(itemStack.getCount()) : "", iX, iY);
            }
            GlStateManager.disableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

}
