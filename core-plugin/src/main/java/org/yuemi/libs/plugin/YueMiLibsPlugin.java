package org.yuemi.libs.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.libs.api.YueMiLibsApi;

import org.yuemi.libs.plugin.bstats.BStatsService;

public final class YueMiLibsPlugin extends JavaPlugin {

    private YueMiLibsApiImpl api;
    private final int CONFIG_VERSION = 4;

    @Override
    public void onEnable() {
        BStatsService.initialize(this);
        // Load config
        saveDefaultConfig();
        reloadConfig();

        // Check and migrate version
        new ConfigMigrator(this, CONFIG_VERSION).migrate();

        this.api = new YueMiLibsApiImpl(this, getDescription().getVersion());

        // Read item match mode
        String matchModeStr = getConfig().getString("providers.minecraft.match-mode", "ID").toUpperCase();
        org.yuemi.libs.plugin.items.MatchMode matchMode;
        try {
            matchMode = org.yuemi.libs.plugin.items.MatchMode.valueOf(matchModeStr);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Invalid match-mode '" + matchModeStr + "' configured. Falling back to ID.");
            matchMode = org.yuemi.libs.plugin.items.MatchMode.ID;
        }

        // Register default vanilla item provider
        api.getItemsImpl().registerProvider("minecraft", new org.yuemi.libs.plugin.items.MinecraftItemProvider(matchMode));

        // Register CraftEngine item provider if enabled
        boolean craftEngineEnabled = getConfig().getBoolean("hooks.items.craftengine.enabled", true);
        if (craftEngineEnabled && getServer().getPluginManager().isPluginEnabled("CraftEngine")) {
            api.getItemsImpl().registerProvider("craftengine", new org.yuemi.libs.plugin.items.CraftEngineItemProvider());
            getLogger().info("Successfully hooked into CraftEngine for items!");
        }

        // Register Nexo item provider if enabled
        boolean nexoEnabled = getConfig().getBoolean("hooks.items.nexo.enabled", true);
        if (nexoEnabled && getServer().getPluginManager().isPluginEnabled("Nexo")) {
            api.getItemsImpl().registerProvider("nexo", new org.yuemi.libs.plugin.items.NexoItemProvider());
            getServer().getPluginManager().registerEvents(new org.yuemi.libs.plugin.listeners.NexoItemsLoadedListener(this), this);
            getLogger().info("Successfully hooked into Nexo for items!");
        }

        // Register ItemsAdder item provider if enabled
        boolean itemsAdderEnabled = getConfig().getBoolean("hooks.items.itemsadder.enabled", true);
        if (itemsAdderEnabled && getServer().getPluginManager().isPluginEnabled("ItemsAdder")) {
            api.getItemsImpl().registerProvider("itemsadder", new org.yuemi.libs.plugin.items.ItemsAdderItemProvider());
            getServer().getPluginManager().registerEvents(new org.yuemi.libs.plugin.listeners.ItemsAdderLoadDataListener(this), this);
            getLogger().info("Successfully hooked into ItemsAdder for items!");
        }

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

        // Register GUI provider and listener
        org.yuemi.libs.api.gui.GuiProvider.register(api.getGui());
        org.yuemi.libs.api.items.ItemsApiProvider.register(api.getItems());
        org.yuemi.libs.api.event.EventApiProvider.register(api.getEvents());
        getServer().getPluginManager().registerEvents(new org.yuemi.libs.plugin.gui.GuiListener(this), this);

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
        org.yuemi.libs.api.gui.GuiProvider.register(null);
        org.yuemi.libs.api.items.ItemsApiProvider.register(null);
        org.yuemi.libs.api.event.EventApiProvider.register(null);
        if (api != null) {
            api.getEventsImpl().disable();
            getServer().getServicesManager().unregister(YueMiLibsApi.class, api);
        }
        getLogger().info("YueMi Libs has been disabled.");
    }
}
