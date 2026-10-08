package com.kacimiamine.velvetyextras.module;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.kacimiamine.velvetyextras.VelvetyExtras;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Random;

public class DeathModule extends ModuleImpl {

    @Override
    protected String name() {
        return "Death";
    }

    @Override
    protected String configName() {
        return "death-config.yml";
    }

    // CONFIG
    private boolean randomRespawnEnabled = true;
    private double randomRespawnRadius = 1000.0D;
    private boolean randomRespawnUseDeathLocationAsCenter = true;
    private boolean randomRespawnIgnoreBed = false;
    private boolean randomRespawnIgnoreAnchor = false;

    @Override
    protected void configValues() {
        super.configValues();
        randomRespawnEnabled = getBoolean("random-respawn.enabled", randomRespawnEnabled);
        randomRespawnRadius = getDouble("random-respawn.radius", randomRespawnRadius);
        randomRespawnUseDeathLocationAsCenter = getBoolean("random-respawn.use-death-location-as-center", randomRespawnUseDeathLocationAsCenter);
        randomRespawnIgnoreBed = getBoolean("random-respawn.ignore-bed", randomRespawnIgnoreBed);
        randomRespawnIgnoreAnchor = getBoolean("random-respawn.ignore-anchor", randomRespawnIgnoreAnchor);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!randomRespawnEnabled) return;
        if (event.getRespawnReason() != PlayerRespawnEvent.RespawnReason.DEATH) return;
        if (event.isBedSpawn() && !randomRespawnIgnoreBed) return;
        if (event.isAnchorSpawn() && !randomRespawnIgnoreAnchor) return;

        Player player = event.getPlayer();
        World respawnWorld = VelvetyExtras.getInstance().getServer().getRespawnWorld();
        Location center = randomRespawnUseDeathLocationAsCenter ? player.getLocation() : respawnWorld.getSpawnLocation();

        Random random = new Random();
        double x = random.nextDouble(center.x() - randomRespawnRadius, center.x() + randomRespawnRadius + 1) + 0.5;
        double z = random.nextDouble(center.z() - randomRespawnRadius, center.z() + randomRespawnRadius + 1) + 0.5;

        event.setRespawnLocation(new Location(respawnWorld, x, 256, z));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        Location respawnLocation = event.getRespawnLocation();
        World respawnWorld = respawnLocation.getWorld();

        respawnWorld.getChunkAtAsync(respawnLocation).thenAccept(_ -> {
            double y = respawnWorld.getHighestBlockYAt(respawnLocation) + 1;
            respawnLocation.setY(y);
            event.getPlayer().teleportAsync(respawnLocation);
        });
    }
}
