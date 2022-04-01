package me.gerald.dallas.mod;

import me.gerald.dallas.mod.mods.FullBright;
import me.gerald.dallas.mod.mods.GUI;
import me.gerald.dallas.mod.mods.Velocity;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        //combat
        modules.add(new Velocity());
        //movement
        //render
        modules.add(new FullBright());
        //misc
        //client
        modules.add(new GUI());
    }

    public List<Module> getModules() {
        return modules;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        for(Module module : getModules()) {
            if(module.getClass() == clazz) {
                return (T) module;
            }
        }
        return null;
    }

    public List<Module> getCategory(Module.Category category) {
        List<Module> modules = new ArrayList<>();
        for(Module module : getModules()) {
            if(module.getCategory() == category) {
                modules.add(module);
            }
        }
        return modules;
    }
}
