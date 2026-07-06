package org.yuemi.libs.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class ConfigMigrator {

    private final Plugin plugin;
    private final int targetVersion;

    public ConfigMigrator(@NotNull Plugin plugin, int targetVersion) {
        this.plugin = plugin;
        this.targetVersion = targetVersion;
    }

    public void migrate() {
        FileConfiguration config = plugin.getConfig();
        int currentVersion = config.getInt("config-version", 0);

        if (currentVersion >= targetVersion) {
            return;
        }

        plugin.getLogger().info("Migrating config.yml from version " + currentVersion + " to " + targetVersion + "...");

        // Perform incremental migrations step by step
        for (int v = currentVersion; v < targetVersion; v++) {
            migrateIncrementally(config, v);
        }

        config.set("config-version", targetVersion);
        plugin.saveConfig();
        plugin.getLogger().info("Config migration completed successfully!");
    }

    private void migrateIncrementally(FileConfiguration config, int version) {
        switch (version) {
            case 0:
                // Migration logic from version 0 to 1
                if (!config.contains("hooks.economy.enabled")) {
                    config.set("hooks.economy.enabled", true);
                }
                if (!config.contains("hooks.economy.provider")) {
                    config.set("hooks.economy.provider", "Vault");
                }
                break;
            case 1:
                // Migration logic from version 1 to 2
                if (!config.contains("providers.minecraft.match-mode")) {
                    config.set("providers.minecraft.match-mode", "ID");
                }
                break;
            case 2:
                // Migration logic from version 2 to 3
                if (!config.contains("hooks.items.craftengine.enabled")) {
                    config.set("hooks.items.craftengine.enabled", true);
                }
                break;
            case 3:
                // Migration logic from version 3 to 4
                if (!config.contains("hooks.items.nexo.enabled")) {
                    config.set("hooks.items.nexo.enabled", true);
                }
                if (!config.contains("hooks.items.itemsadder.enabled")) {
                    config.set("hooks.items.itemsadder.enabled", true);
                }
                break;
            default:
                break;
        }
    }
}
