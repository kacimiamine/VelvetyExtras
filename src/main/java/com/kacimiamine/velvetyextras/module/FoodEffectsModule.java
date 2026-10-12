package com.kacimiamine.velvetyextras.module;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodEffectsModule extends ModuleImpl {

    private final Map<Material, List<PotionEffect>> foodEffects = new HashMap<>();

    @Override
    protected String name() {
        return "Food Effects";
    }

    @Override
    protected String configName() {
        return "food-effects-config.yml";
    }

    @Override
    protected void configValues() {
        super.configValues();
        ConfigurationSection food = getSection("food");
        if (food == null) return;

        for (String foodName : food.getKeys(false)) {
            Material material = Material.matchMaterial(foodName);
            if (material == null) continue;

            ConfigurationSection foodSection = food.getConfigurationSection(foodName);
            if (foodSection == null) return;
            List<PotionEffect> effects = new ArrayList<>();

            for (String effect : foodSection.getKeys(false)) {
                PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(effect));
                if (type == null) continue;

                ConfigurationSection e = foodSection.getConfigurationSection(effect);
                if (e == null) continue;
                effects.add(new PotionEffect(
                        type,
                        e.getInt("duration-seconds", 10) * 20,
                        e.getInt("amplifier", 0),
                        false,
                        e.getBoolean("show-particles", true)
                ));
            }
            foodEffects.put(material, effects);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFoodConsume(PlayerItemConsumeEvent event) {
        Material material = event.getItem().getType();
        if (!foodEffects.containsKey(material)) return;
        event.getPlayer().addPotionEffects(foodEffects.get(material));
    }
}
