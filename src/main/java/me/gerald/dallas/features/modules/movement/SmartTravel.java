package me.gerald.dallas.features.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.BooleanSetting;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.BaritoneHelper;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class SmartTravel extends Module {
    public BooleanSetting healthLogout = new BooleanSetting("HealthLogout", true, "Logout if your health drops below a certain level.");
    public NumberSetting health = new NumberSetting("Health", 10, 1, 20, "Health to log at.", () -> healthLogout.getValue());
    public BooleanSetting wallLogout = new BooleanSetting("WallLogout", true, "If elytra flying and you hit a wall module is toggled and you log.");

    public boolean overrodeProcess = false;
    public boolean elytraTraveling = false;
    public BlockPos targetPos = null;

    public SmartTravel() {
        super("SmartTravel", Category.MOVEMENT, "Will travel to a set of coords then log when reached.");
    }

    @Override
    public void onEnable() {
        if (nullCheck()) return;
        if (!overrodeProcess) {
            for (ItemStack armor : mc.player.getArmorInventoryList()) {
                if (armor.getItem().equals(Items.ELYTRA) && mc.player.isElytraFlying()) {
                    elytraTraveling = true;
                    MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Starting to fly forward...", MessageUtil.MessageType.INFO);
                    return;
                }
            }
            if (targetPos == null) {
                MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Please perform the travel comand and set your target XYZ location.", MessageUtil.MessageType.ERROR);
                toggle();
                return;
            }
            overrodeProcess = true;
            BaritoneHelper.gotoPos(targetPos.getX(), targetPos.getZ());
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Starting to travel with Baritone...", MessageUtil.MessageType.INFO);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        if (targetPos == null) return;
        if (mc.player.getHealth() <= health.getValue() && !mc.player.isDead && healthLogout.getValue()) {
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged because your health dropped below " + healthLogout.getValue() + " and was at " + ChatFormatting.GRAY + "[" + mc.player.getHealth() + "]" + ChatFormatting.RESET + ".")));
            toggle();
            return;
        }
        if (elytraTraveling) {
            if (mc.player.collidedHorizontally && wallLogout.getValue()) {
                Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged because you hit a wall while flying.")));
                toggle();
                return;
            }
            mc.player.moveForward = 1f;
        }
        if (mc.player.getDistance(targetPos.getX(), mc.player.posY, targetPos.getZ()) <= 5) {
            if (!elytraTraveling) {
                overrodeProcess = false;
                BaritoneHelper.stop();
            }
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Arrived at location stopping, toggling, and logging.", MessageUtil.MessageType.INFO);
            Objects.requireNonNull(mc.getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("Logged because you arrived at SmartTravel location.")));
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if (elytraTraveling)
            elytraTraveling = false;
        if (overrodeProcess) {
            overrodeProcess = false;
            BaritoneHelper.stop();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "SmartTravel", "Stopping Baritone...", MessageUtil.MessageType.INFO);
        }
    }
}
