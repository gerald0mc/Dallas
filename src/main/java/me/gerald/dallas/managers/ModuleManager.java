package me.gerald.dallas.managers;

import me.gerald.dallas.features.module.Module;
import me.gerald.dallas.utils.ReflectionUtil;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<?>, Module> moduleMap = new HashMap<>();

    public ModuleManager() {
        ReflectionUtil.getSubclasses(Module.class).forEach(module -> {
            if (!Modifier.isAbstract(module.getModifiers())) {
                try {
                    modules.add(module.getDeclaredConstructor().newInstance());
                } catch (Exception exception) {
                    // this only fails if the constructor throws an exception or has parameters
                    // rethrow because it shouldn't happen
                    throw new RuntimeException("Could not initialize " + module, exception);
                }
            }
        });
        modules.forEach(module -> moduleMap.put(module.getClass(), module));
        modules.forEach(Module::registerValues);
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

    public int getAmountPerCat(Module.Category category) {
        int moduleTot = 0;
        for (Module module : getModules()) {
            if (module.getCategory() == category) {
                moduleTot++;
            }
        }
        return moduleTot;
    }
}
