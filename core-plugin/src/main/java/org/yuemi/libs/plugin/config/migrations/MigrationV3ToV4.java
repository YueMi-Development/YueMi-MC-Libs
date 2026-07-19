package org.yuemi.libs.plugin.config.migrations;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationV3ToV4 implements MigrationStep {
    @Override
    public int getTargetVersion() {
        return 4;
    }

    @Override
    public void migrate(@NotNull FileConfiguration config) {
        if (!config.contains("hooks.items.nexo.enabled")) {
            config.set("hooks.items.nexo.enabled", true);
        }
        if (!config.contains("hooks.items.itemsadder.enabled")) {
            config.set("hooks.items.itemsadder.enabled", true);
        }
    }
}
