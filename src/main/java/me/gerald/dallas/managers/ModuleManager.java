package me.gerald.dallas.managers;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.client.DallasBot;
import me.gerald.dallas.features.module.client.GUI;
import me.gerald.dallas.features.module.combat.*;
import me.gerald.dallas.features.module.hud.coordinates.Coordinates;
import me.gerald.dallas.features.module.hud.cps.CPS;
import me.gerald.dallas.features.module.hud.crystalcount.CrystalCount;
import me.gerald.dallas.features.module.hud.gapplecount.GappleCount;
import me.gerald.dallas.features.module.hud.ping.Ping;
import me.gerald.dallas.features.module.hud.server.Server;
import me.gerald.dallas.features.module.hud.totemcount.TotemCount;
import me.gerald.dallas.features.module.hud.watermark.Watermark;
import me.gerald.dallas.features.module.hud.xpcount.XPCount;
import me.gerald.dallas.features.module.misc.*;
import me.gerald.dallas.features.module.render.DamageESP;
import me.gerald.dallas.features.module.render.Waypoints;

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
        modules.add(new AutoBurrow());
        modules.add(new AutoKit());
        modules.add(new CevPlace());
        modules.add(new CrystalAura());
        modules.add(new FakePearl());
        modules.add(new Velocity());
        //hud
        modules.add(new Coordinates());
        modules.add(new CPS());
        modules.add(new CrystalCount());
        modules.add(new GappleCount());
        modules.add(new Ping());
        modules.add(new Server());
        modules.add(new TotemCount());
        modules.add(new Watermark());
        modules.add(new XPCount());
        //misc
        modules.add(new AutoGG());
        modules.add(new Chat());
        modules.add(new FakePlayer());
        modules.add(new MCF());
        modules.add(new Spammer());
        modules.add(new TexasFacts());
        modules.add(new WebhookSpammer());
        modules.add(new Window());
        //movement
        //render
        modules.add(new DamageESP());
        modules.add(new Waypoints());
    }

    public List<Module> getModules() {
        return modules;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        for (Module module : getModules()) {
            if (module.getClass() == clazz) {
                return (T) module;
            }
        }
        return null;
    }

    public List<Module> getCategory(Module.Category category) {
        List<Module> modules = new ArrayList<>();
        for (Module module : getModules()) {
            if (module.getCategory() == category) {
                modules.add(module);
            }
        }
        return modules;
    }
}
