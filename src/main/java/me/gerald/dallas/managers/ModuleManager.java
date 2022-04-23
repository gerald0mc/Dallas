package me.gerald.dallas.managers;

import me.gerald.dallas.features.module.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.gerald.dallas.utils.ReflectionUtil.REFLECTIONS;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<?>, Module> moduleMap = new HashMap<>();


    // haven't actually tested but it should work lol
    public ModuleManager() {
        REFLECTIONS.getSubTypesOf(Module.class).forEach(module -> {
            try {
                modules.add(module.getDeclaredConstructor().newInstance());
            } catch (Exception exception) {
                // this only really fails if the constructor throws an exception or has parameters
                exception.printStackTrace();
            }
        });

//        //client
//        modules.add(new Console());
//        modules.add(new DallasBot());
//        modules.add(new GUI());
//        //combat
//        modules.add(new AntiTrap());
//        modules.add(new AutoBurrow());
//        modules.add(new AutoKit());
//        modules.add(new CevPlace());
//        modules.add(new CrystalAura());
//        modules.add(new FakePearl());
//        modules.add(new MCP());
//        modules.add(new TotemPopCounter());
//        //hud
//        modules.add(new Armor());
//        modules.add(new me.gerald.dallas.features.module.hud.arraylist.ArrayList());
//        modules.add(new Coordinates());
//        modules.add(new CPS());
//        modules.add(new CrystalCount());
//        modules.add(new GappleCount());
//        modules.add(new Notifications());
//        modules.add(new PacketLog());
//        modules.add(new Ping());
//        modules.add(new Server());
//        modules.add(new TotemCount());
//        modules.add(new Watermark());
//        modules.add(new XPCount());
//        //misc
//        modules.add(new AutoGG());
//        modules.add(new Chat());
//        modules.add(new Emojis());
//        modules.add(new FakePlayer());
//        modules.add(new NameChanger());
//        modules.add(new Spammer());
//        modules.add(new WebhookSpammer());
//        modules.add(new Window());
//        //movement
//        modules.add(new Dive());
//        modules.add(new Sprint());
//        //render
//        modules.add(new Chams());
//        modules.add(new DamageESP());
//        modules.add(new ItemESP());
//        modules.add(new Nametags());
//        modules.add(new Waypoints());

        modules.forEach(module -> moduleMap.put(module.getClass(), module));
    }

    public List<Module> getModules() {
        return modules;
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        return clazz.cast(moduleMap.get(clazz));
    }

    public List<Module> getCategory(Module.Category category) {
        return modules.stream().filter(module -> module.getCategory() == category).collect(Collectors.toList());
    }
}
