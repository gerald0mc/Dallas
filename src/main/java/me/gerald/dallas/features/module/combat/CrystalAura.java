package me.gerald.dallas.features.module.combat;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.setting.settings.NumberSetting;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CrystalAura extends Module {
    public CrystalAura() {
        super("CrystalAura", Module.Category.COMBAT, "NOT AUTO CRYSTAL!!!#!#!@#");
    }

    public NumberSetting range = register(new NumberSetting("Range", 4, 1, 6));
    public NumberSetting delayMS = register(new NumberSetting("DelayMS", 50, 0, 500));

    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        for(Entity entity : mc.world.loadedEntityList) {
            if(mc.player.getDistance(entity) <= range.getValue()) {
                if (entity instanceof EntityEnderCrystal) {
                    if(timer.passedMs((long) delayMS.getValue())) {
                        mc.playerController.attackEntity(mc.player, entity);
                        timer.reset();
                    }
                }
            }
        }
    }
}
