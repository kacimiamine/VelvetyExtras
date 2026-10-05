package com.kacimiamine.velvetyextras.module;

import com.destroystokyo.paper.MaterialSetTag;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class MinecartBoatSplitModule extends ModuleImpl {

    @Override
    protected String name() {
        return "MinecartChest & BoatChest split";
    }

    @Override
    protected String configName() {
        return "minecart-boat-split-config.yml";
    }

    // CONFIG
    private boolean minecartSplitEnabled = true;
    private boolean minecartSplitOnlyWhenSneaking = true;
    private boolean boatSplitEnabled = true;
    private boolean boatSplitOnlyWhenSneaking = true;

    @Override
    protected void configValues() {
        super.configValues();
        minecartSplitEnabled = getBoolean("minecart-split.enabled", minecartSplitEnabled);
        minecartSplitOnlyWhenSneaking = getBoolean("minecart-split.only-when-sneaking", minecartSplitOnlyWhenSneaking);
        boatSplitEnabled = getBoolean("boat-split.enabled", boatSplitEnabled);
        boatSplitOnlyWhenSneaking = getBoolean("boat-split.only-when-sneaking", boatSplitOnlyWhenSneaking);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        if (event.getAttacker() == null || !(event.getAttacker() instanceof Player player)) return;

        Entity vehicle = event.getVehicle();
        Location location = vehicle.getLocation();

        // Minecart
        if (vehicle instanceof Minecart minecart) {
            if (!minecartSplitEnabled) return;
            if (minecartSplitOnlyWhenSneaking && !player.isSneaking()) return;

            event.setCancelled(true);
            vehicle.remove();

            Material minecartMaterial = minecart.getMinecartMaterial();
            switch (minecartMaterial) {
                case CHEST_MINECART -> location.getWorld().dropItemNaturally(location, new ItemStack(Material.CHEST));
                case FURNACE_MINECART -> location.getWorld().dropItemNaturally(location, new ItemStack(Material.FURNACE));
                case HOPPER_MINECART -> location.getWorld().dropItemNaturally(location, new ItemStack(Material.HOPPER));
                case TNT_MINECART -> location.getWorld().dropItemNaturally(location, new ItemStack(Material.TNT));
            }
            location.getWorld().dropItemNaturally(location, new ItemStack(Material.MINECART));
            return;
        }

        // Boats
        if (vehicle instanceof ChestBoat boat) {
            if (!boatSplitEnabled) return;
            if (boatSplitOnlyWhenSneaking && !player.isSneaking()) return;

            Material boatMaterial = boat.getBoatMaterial();
            if (!MaterialSetTag.ITEMS_CHEST_BOATS.isTagged(boatMaterial)) return;

            boatMaterial = Material.matchMaterial(boatMaterial.toString().replace("_CHEST", ""));
            if (boatMaterial == null) return;

            event.setCancelled(true);
            vehicle.remove();

            location.getWorld().dropItemNaturally(location, new ItemStack(Material.CHEST));
            location.getWorld().dropItemNaturally(location, new ItemStack(boatMaterial));
        }
    }
}
