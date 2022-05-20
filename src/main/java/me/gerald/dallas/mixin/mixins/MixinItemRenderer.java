package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.render.ViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author bush
 * @since 5/6/2022
 */
@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "rotateArm", at = @At("HEAD"), cancellable = true)
    private void rotateArm(float tickProgress, CallbackInfo callback) {
        // For people reading this that are confused about the method "rotateAroundXAndY",
        // that only changes lighting. This method is what actually rotates held items.
        ViewModel module = Yeehaw.INSTANCE.moduleManager.getModule(ViewModel.class);
        if (module.isEnabled() && module.noSway.getValue()) {
            callback.cancel();
        }
    }

    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entityLivingBaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        ViewModel module = Yeehaw.INSTANCE.moduleManager.getModule(ViewModel.class);
        if (module.isEnabled() && module.viewModel.getValue()) {
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.scale(module.scaleX.getValue() / 100F, module.scaleY.getValue() / 100F, module.scaleZ.getValue() / 100F);
                if(Mouse.getEventButtonState() && Mouse.getEventButton() == 1) {
                    if(heldStack.getItem() instanceof ItemSword) {
                        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
                    }
                } else {
                    GlStateManager.translate(module.translateX.getValue() / 200F, module.translateY.getValue() / 200F, module.translateZ.getValue() / 200F);
                }
                GlStateManager.rotate(module.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(module.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(module.rotateZ.getValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.scale((module.bothSame.getValue() ? module.scaleX.getValue() : module.rightScaleX.getValue()) / 100F, (module.bothSame.getValue() ? module.scaleY.getValue() : module.rightScaleY.getValue()) / 100F, (module.bothSame.getValue() ? module.scaleZ.getValue() : module.rightScaleZ.getValue()) / 100F);
                GlStateManager.translate((module.bothSame.getValue() ? -module.translateX.getValue() : -module.rightTranslateX.getValue()) / 200F, (module.bothSame.getValue() ? module.translateY.getValue() : module.rightTranslateY.getValue()) / 200F, (module.bothSame.getValue() ? module.translateZ.getValue() : module.rightTranslateZ.getValue()) / 200F);
                GlStateManager.rotate(module.bothSame.getValue() ? -module.rotateX.getValue() : -module.rightRotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(module.bothSame.getValue() ? module.rotateY.getValue() : module.rightRotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(module.bothSame.getValue() ? module.rotateZ.getValue() : module.rightRotateZ.getValue(), 0, 0, 1);
            }
        }
    }
}
