package me.gerald.dallas.features.modules.combat;

import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CrystalAura extends Module {
    public NumberSetting range = new NumberSetting("Range", 4, 1, 6);
    public TimerUtil breakTimer = new TimerUtil();

    public CrystalAura() {
        super("CrystalAura", Module.Category.COMBAT, "NOT AUTO CRYSTAL!!!#!#!@#");
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (nullCheck()) return;
        for (Entity entity : mc.world.loadedEntityList) {
            if (mc.player.getDistance(entity) <= range.getValue()) {
                if (entity instanceof EntityEnderCrystal) {
                    mc.playerController.attackEntity(mc.player, entity);
                    breakTimer.reset();
                }
            }
        }
    }
}
