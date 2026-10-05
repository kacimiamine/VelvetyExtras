package com.kacimiamine.velvetyextras.module;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import com.kacimiamine.velvetyextras.config.VelvetyExtrasConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public abstract class ModuleImpl implements Module, Listener {

    protected static VelvetyExtras plugin = VelvetyExtras.getInstance();

    protected YamlConfiguration config;
    protected File configFile;

    protected boolean enabled = true;

    protected abstract String name();

    protected abstract String configName();

    @Override
    public void enable() {
        initConfig();
        if (!isEnabled()) return;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        VelvetyExtras.logger().info("{} module is enabled!", this.name());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
        VelvetyExtras.logger().info("{} module is disabled!", this.name());
    }

    protected void initConfig() {
        configFile = new File(VelvetyExtrasConfig.configDir, configName());
        config = YamlConfiguration.loadConfiguration(configFile);
        configValues();

        try {
            config.save(configFile);
        } catch (IOException e) {
            VelvetyExtras.logger().error("Config file ({}) can not be saved", configFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    protected int getInt(String path, int value) {
        return VelvetyExtrasConfig.getInt(this.config, path, value);
    }

    protected double getDouble(String path, double value) {
        return VelvetyExtrasConfig.getDouble(this.config, path, value);
    }

    protected String getString(String path, String value) {
        return VelvetyExtrasConfig.getString(this.config, path, value);
    }

    protected boolean getBoolean(String path, boolean value) {
        return VelvetyExtrasConfig.getBoolean(this.config, path, value);
    }

    protected void configValues() {
        enabled = VelvetyExtrasConfig.getBoolean(config, "enabled", enabled);
    }
}
