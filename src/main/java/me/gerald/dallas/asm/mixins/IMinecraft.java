package me.gerald.dallas.asm.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({Minecraft.class})
public interface IMinecraft {
    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimerAccessor(int timer);
}
