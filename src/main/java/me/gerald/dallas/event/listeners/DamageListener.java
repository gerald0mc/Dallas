package me.gerald.dallas.event.listeners;

import me.gerald.dallas.Yeehaw;
import me.gerald.dallas.event.events.DamageEvent;
import me.gerald.dallas.features.modules.render.DamageESP;
import me.gerald.dallas.managers.module.Module;
import me.gerald.dallas.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;

public class DamageListener {
    public HashMap<EntityLivingBase, Float> entityHealthMap = new HashMap<>();
    public TimerUtil timer = new TimerUtil();

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (Module.nullCheck()) return;
        for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;
                if (!entityHealthMap.containsKey(entity)) {
                    entityHealthMap.put(entity, entity.getHealth());
                } else {
                    if (entityHealthMap.get(entity) > entity.getHealth()) {
                        if (!timer.passedMs(Yeehaw.INSTANCE.moduleManager.getModule(DamageESP.class).isEnabled() ? (long) (Yeehaw.INSTANCE.moduleManager.getModule(DamageESP.class).timeBetweenChecks.getValue() * 1000) : 50))
                            return;
                        MinecraftForge.EVENT_BUS.post(new DamageEvent.Damage(entity, (int) (entityHealthMap.get(entity) - entity.getHealth())));
                        entityHealthMap.replace(entity, entity.getHealth());
                        timer.reset();
                    } else if (entityHealthMap.get(entity) < entity.getHealth()) {
                        if (!timer.passedMs(Yeehaw.INSTANCE.moduleManager.getModule(DamageESP.class).isEnabled() ? (long) (Yeehaw.INSTANCE.moduleManager.getModule(DamageESP.class).timeBetweenChecks.getValue() * 1000) : 50))
                            return;
                        MinecraftForge.EVENT_BUS.post(new DamageEvent.Damage(entity, (int) (entity.getHealth() - entityHealthMap.get(entity))));
                        entityHealthMap.replace(entity, entity.getHealth());
                        timer.reset();
                    }
                }
            }
        }
    }
}
