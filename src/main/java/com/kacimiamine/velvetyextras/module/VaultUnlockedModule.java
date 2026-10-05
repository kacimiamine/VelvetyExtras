package com.kacimiamine.velvetyextras.module;

import io.papermc.paper.event.block.VaultChangeStateEvent;
import org.bukkit.block.Vault;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class VaultUnlockedModule extends ModuleImpl {

    @Override
    protected String name() {
        return "Vault Unlocked";
    }

    @Override
    protected String configName() {
        return "vault-unlocked-config.yml";
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onVaultInteract(VaultChangeStateEvent event) {
        if (!(event.getBlock().getState() instanceof Vault vault)) return;
        vault.getRewardedPlayers().forEach(vault::removeRewardedPlayer);
        vault.update();
    }
}
