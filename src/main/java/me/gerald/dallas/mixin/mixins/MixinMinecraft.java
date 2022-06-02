package me.gerald.dallas.mixin.mixins;

import me.gerald.dallas.mixin.duck.IMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MixinMinecraft implements IMinecraft {

    @Shadow @Final private Timer timer;

    @Override
    public void setTimerSpeed(float speed) {
        ((ITimer) timer).setTickLength(50.0f / speed);
    }
}
