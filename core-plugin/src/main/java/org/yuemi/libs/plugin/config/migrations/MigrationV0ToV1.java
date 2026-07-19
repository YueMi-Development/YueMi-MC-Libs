package org.yuemi.libs.plugin.config.migrations;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationV0ToV1 implements MigrationStep {
    @Override
    public int getTargetVersion() {
        return 1;
    }

    @Override
    public void migrate(@NotNull FileConfiguration config) {
        if (!config.contains("hooks.economy.enabled")) {
            config.set("hooks.economy.enabled", true);
        }
        if (!config.contains("hooks.economy.provider")) {
            config.set("hooks.economy.provider", "Vault");
        }
    }
}
