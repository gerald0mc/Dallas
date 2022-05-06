package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.features.modules.render.ViewModel;
import net.minecraft.client.renderer.ItemRenderer;
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
        if (ViewModel.INSTANCE.isEnabled() && ViewModel.INSTANCE.noSway.getValue()) {
            callback.cancel();
        }
    }
}
