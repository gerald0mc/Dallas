package me.gerald.dallas.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.BaritoneHelper;
import me.gerald.dallas.utils.InventoryUtil;
import me.gerald.dallas.utils.MessageUtil;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ObbyBot extends Module {
    public boolean overrodeProcess = false;

    public ObbyBot() {
        super("ObbyBot", Category.MISC, "Automatically mines obby for you.");
    }

    @Override
    public void onEnable() {
        if (!overrodeProcess) {
            overrodeProcess = true;
            BaritoneHelper.getBaritone().getMineProcess().mine(Blocks.OBSIDIAN);
            MessageUtil.sendMessage(ChatFormatting.BOLD + "ObbyBot", "Starting to mine for obsidian...", true);
        }
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (!overrodeProcess) return;
        if (InventoryUtil.isInventoryFull(true)) {
            overrodeProcess = false;
            BaritoneHelper.stop();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "ObbyBot", "Stopping baritone since your inventory is full.", true);
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if (overrodeProcess) {
            overrodeProcess = false;
            BaritoneHelper.stop();
            MessageUtil.sendMessage(ChatFormatting.BOLD + "ObbyBot", "Stoping baritone...", true);
        }
    }
}
