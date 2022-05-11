package me.gerald.dallas.features.modules.baritone;

import me.gerald.dallas.managers.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Chase extends Module {
    public Chase() {
        super("Chase", Category.BARITONE, "Chases a person running from you.");
    }

    public EntityPlayer chaseTarget = null;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if(nullCheck()) return;
        if (chaseTarget == null) {
            for(EntityPlayer player : mc.world.playerEntities) {

            }
        } else {

        }
    }
}
