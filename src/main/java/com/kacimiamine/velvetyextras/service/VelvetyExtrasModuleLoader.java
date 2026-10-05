package com.kacimiamine.velvetyextras.service;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import com.kacimiamine.velvetyextras.module.Module;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class VelvetyExtrasModuleLoader {

    private static final Set<Module> loadedModules = new HashSet<>();

    public static void registerModules(VelvetyExtras plugin) {
        ServiceLoader<Module> loader = ServiceLoader.load(Module.class, plugin.getClass().getClassLoader());

        for (Module module : loader) {
            module.enable();
            if (module.isEnabled()) loadedModules.add(module);
        }
    }

    public static void unregisterAllLoadedModules() {
        loadedModules.forEach(Module::disable);
        loadedModules.clear();
    }
}
