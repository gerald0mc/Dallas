package me.gerald.dallas.managers;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.features.module.hud.armor.Armor;
import me.gerald.dallas.features.module.misc.*;
import me.gerald.dallas.features.module.movement.Sprint;
import me.gerald.dallas.features.module.render.*;
import me.gerald.dallas.features.module.client.*;
import me.gerald.dallas.features.module.combat.*;
import me.gerald.dallas.features.module.hud.coordinates.Coordinates;
import me.gerald.dallas.features.module.hud.cps.CPS;
import me.gerald.dallas.features.module.hud.crystalcount.CrystalCount;
import me.gerald.dallas.features.module.hud.gapplecount.GappleCount;
import me.gerald.dallas.features.module.hud.notification.Notifications;
import me.gerald.dallas.features.module.hud.totemcount.TotemCount;
import me.gerald.dallas.features.module.hud.watermark.Watermark;
import me.gerald.dallas.features.module.hud.xpcount.XPCount;
import me.gerald.dallas.features.module.hud.ping.Ping;
import me.gerald.dallas.features.module.hud.server.Server;

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
        modules.add(new TotemPopCounter());
        //hud
        modules.add(new Armor());
        modules.add(new Coordinates());
        modules.add(new CPS());
        modules.add(new CrystalCount());
        modules.add(new GappleCount());
        modules.add(new Notifications());
        modules.add(new Ping());
        modules.add(new Server());
        modules.add(new TotemCount());
        modules.add(new Watermark());
        modules.add(new XPCount());
        //misc
        modules.add(new AutoGG());
        modules.add(new Chat());
        modules.add(new FakePlayer());
        modules.add(new Spammer());
        modules.add(new WebhookSpammer());
        modules.add(new Window());
        //movement
        modules.add(new Sprint());
        //render
        modules.add(new ChorusPredict());
        modules.add(new DamageESP());
        modules.add(new ItemESP());
        modules.add(new Nametags());
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
