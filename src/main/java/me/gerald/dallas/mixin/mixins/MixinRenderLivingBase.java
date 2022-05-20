package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.features.modules.render.DeathAnimation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    @Inject(method = "applyRotations(Lnet/minecraft/entity/EntityLivingBase;FFF)V", at = @At(value = "HEAD"), cancellable = true)
    public void applyRotations(EntityLivingBase entityLiving, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        DeathAnimation module = Yeehaw.INSTANCE.moduleManager.getModule(DeathAnimation.class);
        if(module.isEnabled()) {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);

            if (entityLiving.deathTime > 0) {
                float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
                f = MathHelper.sqrt(f);

                if (f > 1.0F) {
                    f = 1.0F;
                }

                GlStateManager.rotate(f * 90, 0, 0, 1);
            } else {
                String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());

                if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                    GlStateManager.translate(0.0F, entityLiving.height + 0.1F, 0.0F);
                    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                }
            }
            ci.cancel();
        }
    }
}
