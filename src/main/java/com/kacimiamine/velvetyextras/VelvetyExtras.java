package com.kacimiamine.velvetyextras;

import com.kacimiamine.velvetyextras.config.VelvetyExtrasConfig;
import com.kacimiamine.velvetyextras.service.VelvetyExtrasModuleLoader;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;

public final class VelvetyExtras extends JavaPlugin {

    private final String pluginName = this.getName();

    private static VelvetyExtras plugin;
    private static ComponentLogger logger;

    @Override
    public void onEnable() {
        plugin = this;
        logger = this.getComponentLogger();

        VelvetyExtrasConfig.init();
        VelvetyExtrasModuleLoader.registerModules(this);

        logger.info("{} is successfully enabled!", pluginName);
    }

    @Override
    public void onDisable() {
        logger.info("{} is successfully disabled!", pluginName);
    }

    public static VelvetyExtras getInstance() {
        return plugin;
    }

    public static ComponentLogger logger() {
        return logger;
    }
}
