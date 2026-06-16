package org.yuemi.libs.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.libs.api.YueMiLibsApi;

public final class YueMiLibsPlugin extends JavaPlugin {

    private YueMiLibsApiImpl api;
    private final int CONFIG_VERSION = 1;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        reloadConfig();

        // Check and migrate version
        new ConfigMigrator(this, CONFIG_VERSION).migrate();

        this.api = new YueMiLibsApiImpl();

        // Check economy configuration
        boolean economyEnabled = getConfig().getBoolean("hooks.economy.enabled", true);
        String economyProvider = getConfig().getString("hooks.economy.provider", "Vault").toUpperCase();

        if (economyEnabled) {
            switch (economyProvider) {
                case "VAULT":
                    if (getServer().getPluginManager().isPluginEnabled("Vault")) {
                        api.getEconomyImpl().registerProvider(new org.yuemi.libs.plugin.economy.VaultEconomyProvider());
                        getLogger().info("Successfully hooked into Vault Economy!");
                    } else {
                        getLogger().warning("Vault Economy is configured but Vault plugin is not enabled!");
                    }
                    break;
                default:
                    getLogger().warning("Unknown or unsupported economy provider configured: " + economyProvider);
                    break;
            }
        }

        getServer().getServicesManager().register(
                YueMiLibsApi.class,
                api,
                this,
                ServicePriority.Normal
        );

        getLogger().info("YueMi Libs has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (api != null) {
            getServer().getServicesManager().unregister(YueMiLibsApi.class, api);
        }
        getLogger().info("YueMi Libs has been disabled.");
    }
}
