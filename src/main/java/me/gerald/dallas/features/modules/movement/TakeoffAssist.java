package me.gerald.dallas.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.mixin.duck.IMinecraft;
import me.gerald.dallas.mixin.mixins.ITimer;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TakeoffAssist extends Module {
    public TakeoffAssist() {
        super("TakeoffAssist", Category.MOVEMENT, "Helps you takeoff by lowering your timer speed.");
    }

    public NumberSetting timerSpeed = new NumberSetting("TimerSpeed", 0.4f, 0.1f, 1, "Timer speed to slow you down to.");

    @Override
    public void onEnable() {
        if (!isWearingElytra()) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "TakeoffAssist", "You not wearing a elytra toggling.", MessageUtil.MessageType.ERROR);
            toggle();
            return;
        }
        if (mc.player.isElytraFlying()) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "TakeoffAssist", "You are already Elytra flying toggling.", MessageUtil.MessageType.ERROR);
            toggle();
            return;
        }
        if (!mc.player.onGround) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "TakeoffAssist", "Please be on the ground to make this module work.", MessageUtil.MessageType.INFO);
            toggle();
            return;
        }
        mc.player.jump();
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) {
            toggle();
            return;
        }
        if (!mc.player.isElytraFlying()) {
            ((IMinecraft) mc).setTimerSpeed(timerSpeed.getValue());
        } else {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "TakeoffAssist", "You are now flying toggling.", MessageUtil.MessageType.INFO);
            toggle();
            return;
        }
        if (mc.player.onGround) {
            MessageUtil.sendMessage(ChatFormatting.BOLD + "TakeoffAssist", "You have landed on the ground toggling.", MessageUtil.MessageType.INFO);
            toggle();
        }
    }

    @Override
    public void onDisable() {
        ((IMinecraft) mc).setTimerSpeed(1.0f);
    }

    public boolean isWearingElytra() {
        for (ItemStack stack : mc.player.getArmorInventoryList()) {
            if (stack.equals(Items.ELYTRA)) return true;
        }
        return false;
    }
}
