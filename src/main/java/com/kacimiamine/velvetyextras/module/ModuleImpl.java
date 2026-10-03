package com.kacimiamine.velvetyextras.module;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import com.kacimiamine.velvetyextras.config.VelvetyExtrasConfig;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class ModuleImpl implements Module {

    protected YamlConfiguration config;
    protected File configFile;

    protected boolean enabled = true;

    protected abstract String name();

    protected abstract String configName();

    @Override
    public void enable() {
        initConfig();
        if (!isEnabled()) return;
        VelvetyExtras.logger().info("{} module is enabled!", this.name());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
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

    protected void configValues() {
        enabled = VelvetyExtrasConfig.getBoolean(config, "enabled", enabled);
    }
}
