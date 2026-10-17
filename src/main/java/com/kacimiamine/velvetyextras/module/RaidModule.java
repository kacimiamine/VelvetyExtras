package com.kacimiamine.velvetyextras.module;

import com.google.auto.service.AutoService;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Raider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.raid.RaidSpawnWaveEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AutoService(Module.class)
public class RaidModule extends ModuleImpl{
    @Override
    protected String name() {
        return "Raid";
    }

    @Override
    protected String configName() {
        return "raid-config.yml";
    }

    // CONFIG
    private double spawnIllusionersChance = 0.0D;
    private int spawnIllusionersAmount = 1;
    private boolean spawnIllusionersOnlyIfEvokerPresent = true;

    @Override
    protected void configValues() {
        super.configValues();
        spawnIllusionersChance = getDouble("spawn-illusioners.chance", spawnIllusionersChance);
        spawnIllusionersAmount = getInt("spawn-illusioners.amount", spawnIllusionersAmount);
        spawnIllusionersOnlyIfEvokerPresent = getBoolean("spawn-illusioners.only-if-evoker-present", spawnIllusionersOnlyIfEvokerPresent);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onRaidSpawnWave(RaidSpawnWaveEvent event) {
        if (spawnIllusionersChance == 0.0D || spawnIllusionersChance <= ThreadLocalRandom.current().nextDouble()) return;
        List<Raider> raiders = event.getRaiders();
        if (spawnIllusionersOnlyIfEvokerPresent && raiders.stream().noneMatch(raider -> raider instanceof Evoker)) return;
        World world = event.getWorld();
        Location location = event.getPatrolLeader().getLocation();
        for (int i = 0; i < spawnIllusionersAmount; i++) {
            world.spawn(location, Illusioner.class);
        }
    }
}
