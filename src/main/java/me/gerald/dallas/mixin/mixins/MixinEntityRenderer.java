package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.render.AntiFog;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "setupFog", at = @At("TAIL"))
    private void fogHook(int p_78468_1_, float p_78468_2_, CallbackInfo ci) {
        if (Yeehaw.INSTANCE.moduleManager.getModule(AntiFog.class).isEnabled())
            GlStateManager.disableFog();
    }
}
