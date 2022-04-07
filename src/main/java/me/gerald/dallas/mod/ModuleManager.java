package me.gerald.dallas.mod;

import me.gerald.dallas.mod.mods.client.DallasBot;
import me.gerald.dallas.mod.mods.combat.*;
import me.gerald.dallas.mod.mods.misc.*;
import me.gerald.dallas.mod.mods.render.DamageESP;
import me.gerald.dallas.mod.mods.render.FullBright;
import me.gerald.dallas.mod.mods.client.GUI;
import me.gerald.dallas.mod.mods.render.NameTags;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();
        //client
        modules.add(new DallasBot());
        modules.add(new GUI());
        //combat
        modules.add(new AntiTrap());
        modules.add(new FakePearl());
        modules.add(new Surround());
        modules.add(new Velocity());
        //misc
        modules.add(new AutoGG());
        modules.add(new Chat());
        modules.add(new MCF());
        modules.add(new Spammer());
        modules.add(new TexasFacts());
        //movement
        //render
        modules.add(new DamageESP());
        modules.add(new FullBright());
        modules.add(new NameTags());
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
