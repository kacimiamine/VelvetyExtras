package com.kacimiamine.velvetyextras.config;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class VelvetyExtrasConfig {

    public static File configDir;
    public static int version;

    public static void init() {
        VelvetyExtras plugin = VelvetyExtras.getInstance();

        configDir = new File(plugin.getDataFolder(), "modules");
        if (!configDir.exists() && !configDir.mkdirs()) {
            VelvetyExtras.logger().error("Config directory ({}) can not be created", configDir.getAbsolutePath());
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        version = getInt(config, "config-version", 1);
        try {
            config.save(configFile);
        } catch (IOException e) {
            VelvetyExtras.logger().error("Config file ({}) can not be saved", configFile.getAbsolutePath());
            throw new RuntimeException(e);
        }
    }

    public static int getInt(YamlConfiguration config, String path, int value) {
        if (config.isSet(path)) return config.getInt(path);
        config.set(path, value);
        return value;
    }

    public static double getDouble(YamlConfiguration config, String path, double value) {
        if (config.isSet(path)) return config.getDouble(path);
        config.set(path, value);
        return value;
    }

    public static String getString(YamlConfiguration config, String path, String value) {
        if (config.isSet(path)) return config.getString(path);
        config.set(path, value);
        return value;
    }

    public static boolean getBoolean(YamlConfiguration config, String path, boolean value) {
        if (config.isSet(path)) return config.getBoolean(path);
        config.set(path, value);
        return value;
    }
}
