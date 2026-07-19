package org.yuemi.libs.plugin.config.migrations;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.yuemi.config.api.MigrationStep;

public class MigrationV1ToV2 implements MigrationStep {
    @Override
    public int getTargetVersion() {
        return 2;
    }

    @Override
    public void migrate(@NotNull FileConfiguration config) {
        if (!config.contains("providers.minecraft.match-mode")) {
            config.set("providers.minecraft.match-mode", "ID");
        }
    }
}
