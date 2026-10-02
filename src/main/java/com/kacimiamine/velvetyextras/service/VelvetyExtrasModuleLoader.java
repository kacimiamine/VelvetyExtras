package com.kacimiamine.velvetyextras.service;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import com.kacimiamine.velvetyextras.module.Module;

import java.util.ServiceLoader;

public class VelvetyExtrasModuleLoader {

    public static void registerModules(VelvetyExtras plugin) {
        ServiceLoader<Module> loader = ServiceLoader.load(Module.class, plugin.getClass().getClassLoader());

        for (Module module : loader) {
            module.enable();
        }
    }
}
