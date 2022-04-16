package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.module.render.Nametags;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method = "renderEntityName*", at = @At("HEAD"), cancellable = true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo info) {
        if(Yeehaw.INSTANCE.moduleManager.getModule(Nametags.class).isEnabled()) {
            info.cancel();
        }
    }
}
