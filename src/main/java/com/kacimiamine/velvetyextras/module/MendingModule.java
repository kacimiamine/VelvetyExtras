package com.kacimiamine.velvetyextras.module;

import com.kacimiamine.velvetyextras.VelvetyExtras;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class MendingModule extends ModuleImpl {

    @Override
    protected String name() {
        return "Mending";
    }

    @Override
    protected String configName() {
        return "mending-config.yml";
    }

    // CONFIG
    private boolean mendingVillagerTradesEnabled = true;
    private boolean mendingVillagerTradeReplace = true;
    private String mendingVillagerTradeItemName = "bookshelf";
    private int mendingVillagerTradeItemCount = 1;
    private int mendingVillagerTradeItemPrice = 9;

    @Override
    protected void configValues() {
        super.configValues();
        mendingVillagerTradesEnabled = getBoolean("villager-trades.enabled", mendingVillagerTradesEnabled);
        mendingVillagerTradeReplace = getBoolean("villager-trades.replace.enabled", mendingVillagerTradeReplace);
        mendingVillagerTradeItemName = getString("villager-trades.replace.item-name", mendingVillagerTradeItemName);
        mendingVillagerTradeItemCount = getInt("villager-trades.replace.count", mendingVillagerTradeItemCount);
        mendingVillagerTradeItemPrice = getInt("villager-trades.replace.price", mendingVillagerTradeItemPrice);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onVillagerTrade(VillagerAcquireTradeEvent event) {
        if (mendingVillagerTradesEnabled) return;
        MerchantRecipe oldTradeRecipe = event.getRecipe();
        ItemStack result = oldTradeRecipe.getResult();
        if (result.getType() != Material.ENCHANTED_BOOK) return;

        EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) result.getItemMeta();
        if (!itemMeta.hasStoredEnchant(Enchantment.MENDING)) return;

        if (!mendingVillagerTradeReplace) {
            event.setCancelled(true);
            return;
        }

        Material material = Material.matchMaterial(mendingVillagerTradeItemName);
        if (material == null) {
            VelvetyExtras.logger().error("[{}] Material {} doesn't exist, removing the trade instead!", this.name(), mendingVillagerTradeItemName);
            event.setCancelled(true);
            return;
        }

        MerchantRecipe recipe = new MerchantRecipe(new ItemStack(material, mendingVillagerTradeItemCount),
                oldTradeRecipe.getUses(), oldTradeRecipe.getMaxUses(), oldTradeRecipe.hasExperienceReward());

        recipe.addIngredient(new ItemStack(Material.EMERALD, mendingVillagerTradeItemPrice));
        event.setRecipe(recipe);
    }
}
