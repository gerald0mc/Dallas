package me.gerald.dallas.managers;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.client.DallasBot;
import me.gerald.dallas.features.module.combat.*;
import me.gerald.dallas.features.module.hud.coordinates.Coordinates;
import me.gerald.dallas.features.module.misc.*;
import me.gerald.dallas.features.module.hud.crystalcount.CrystalCount;
import me.gerald.dallas.features.module.hud.watermark.Watermark;
import me.gerald.dallas.features.module.render.DamageESP;
import me.gerald.dallas.features.module.render.FullBright;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.features.module.render.NameTags;

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
        modules.add(new AutoKit());
        modules.add(new CrystalAura());
        modules.add(new FakePearl());
        modules.add(new Surround());
        modules.add(new Velocity());
        //hud
        modules.add(new Coordinates());
        modules.add(new CrystalCount());
        modules.add(new Watermark());
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
