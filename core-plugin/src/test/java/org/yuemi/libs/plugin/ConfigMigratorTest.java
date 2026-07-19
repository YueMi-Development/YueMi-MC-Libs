package org.yuemi.libs.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;
import org.yuemi.libs.plugin.config.migrations.MigrationV0ToV1;
import org.yuemi.libs.plugin.config.migrations.MigrationV1ToV2;
import org.yuemi.libs.plugin.config.migrations.MigrationV2ToV3;
import org.yuemi.libs.plugin.config.migrations.MigrationV3ToV4;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigMigratorTest {

    @Test
    public void testMigrationSteps() {
        FileConfiguration config = new YamlConfiguration();

        // Test V0 to V1
        new MigrationV0ToV1().migrate(config);
        assertTrue(config.getBoolean("hooks.economy.enabled"));
        assertEquals("Vault", config.getString("hooks.economy.provider"));

        // Test V1 to V2
        new MigrationV1ToV2().migrate(config);
        assertEquals("ID", config.getString("providers.minecraft.match-mode"));

        // Test V2 to V3
        new MigrationV2ToV3().migrate(config);
        assertTrue(config.getBoolean("hooks.items.craftengine.enabled"));

        // Test V3 to V4
        new MigrationV3ToV4().migrate(config);
        assertTrue(config.getBoolean("hooks.items.nexo.enabled"));
        assertTrue(config.getBoolean("hooks.items.itemsadder.enabled"));
    }
}
