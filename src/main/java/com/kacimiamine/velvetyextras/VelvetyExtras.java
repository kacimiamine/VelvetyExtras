package com.kacimiamine.velvetyextras;

import com.kacimiamine.velvetyextras.command.VelvetyExtrasCommand;
import com.kacimiamine.velvetyextras.config.VelvetyExtrasConfig;
import com.kacimiamine.velvetyextras.service.VelvetyExtrasModuleLoader;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class VelvetyExtras extends JavaPlugin {

    private final String pluginName = this.getName();

    private static VelvetyExtras plugin;
    private static ComponentLogger logger;

    @Override
    public void onEnable() {
        plugin = this;
        logger = this.getComponentLogger();

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(VelvetyExtrasCommand.createCommand());
        });

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

    public void reload() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            VelvetyExtrasModuleLoader.unregisterAllLoadedModules();
            VelvetyExtrasConfig.init();
            VelvetyExtrasModuleLoader.registerModules(this);

            logger.info("{} reloaded.", pluginName);
        });
    }
}
