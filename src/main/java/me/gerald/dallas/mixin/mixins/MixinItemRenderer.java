package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.render.AntiOverlay;
import me.gerald.dallas.features.modules.render.OldAnimations;
import me.gerald.dallas.features.modules.render.ViewModel;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @Inject(method = "renderOverlays", at = @At("HEAD"), cancellable = true)
    private void renderOverlays(float partialTicks, CallbackInfo ci) {
        //Just cancels all rendering of overlays. (Water, Fire, and Suffocation)
        AntiOverlay module = Yeehaw.INSTANCE.moduleManager.getModule(AntiOverlay.class);
        if (module.isEnabled())
            ci.cancel();
    }

    @ModifyVariable(method = "updateEquippedItem", at = @At("STORE"), index = 4)
    private float updateEquippedItem$ModifyVariable$STORE$F4(float value) {
        return Yeehaw.INSTANCE.moduleManager.getModule(OldAnimations.class).isEnabled() ? 1f : value;
    }
}
