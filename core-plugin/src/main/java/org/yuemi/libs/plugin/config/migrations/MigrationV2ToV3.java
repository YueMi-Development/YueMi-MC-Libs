package org.yuemi.libs.plugin.config.migrations;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationV2ToV3 implements MigrationStep {
    @Override
    public int getTargetVersion() {
        return 3;
    }

    @Override
    public void migrate(@NotNull FileConfiguration config) {
        if (!config.contains("hooks.items.craftengine.enabled")) {
            config.set("hooks.items.craftengine.enabled", true);
        }
    }
}
