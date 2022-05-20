package me.gerald.dallas.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DamageEvent extends Event {
    private final Entity entity;
    private final int amount;

    public DamageEvent(Entity entity, int amount) {
        this.entity = entity;
        this.amount = amount;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getAmount() {
        return amount;
    }

    public static class Damage extends DamageEvent {
        public Damage(Entity entity, int amount) {
            super(entity, amount);
        }
    }

    public static class Heal extends DamageEvent {
        public Heal(Entity entity, int amount) {
            super(entity, amount);
        }
    }
}
