package me.gerald.dallas.event.events;

import me.gerald.dallas.mod.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleToggleEvent extends Event {
    public Module module;

    public ModuleToggleEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public static class Enable extends ModuleToggleEvent{
        public Enable(Module module) {
            super(module);
        }
    }

    public static class Disable extends ModuleToggleEvent{
        public Disable(Module module) {
            super(module);
        }
    }
}
