package org.yuemi.libs.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.logging.Logger;
import static org.mockito.Mockito.*;

public class ConfigMigratorTest {

    private Plugin mockPlugin;
    private FileConfiguration mockConfig;

    @BeforeEach
    public void setUp() {
        mockPlugin = mock(Plugin.class);
        mockConfig = mock(FileConfiguration.class);
        Logger mockLogger = mock(Logger.class);

        when(mockPlugin.getConfig()).thenReturn(mockConfig);
        when(mockPlugin.getLogger()).thenReturn(mockLogger);
    }

    @Test
    public void testMigrationFromVersion0() {
        // Mock current version as 0
        when(mockConfig.getInt("config-version", 0)).thenReturn(0);
        
        // Setup mock configurations
        when(mockConfig.contains("hooks.economy.enabled")).thenReturn(false);
        when(mockConfig.contains("hooks.economy.provider")).thenReturn(false);
        when(mockConfig.contains("providers.minecraft.match-mode")).thenReturn(false);
        when(mockConfig.contains("hooks.items.craftengine.enabled")).thenReturn(false);

        ConfigMigrator migrator = new ConfigMigrator(mockPlugin, 3);
        migrator.migrate();

        // Verify incremental step 0 -> 1 migration
        verify(mockConfig).set("hooks.economy.enabled", true);
        verify(mockConfig).set("hooks.economy.provider", "Vault");

        // Verify incremental step 1 -> 2 migration
        verify(mockConfig).set("providers.minecraft.match-mode", "ID");

        // Verify incremental step 2 -> 3 migration
        verify(mockConfig).set("hooks.items.craftengine.enabled", true);

        // Verify target version save
        verify(mockConfig).set("config-version", 3);
        verify(mockPlugin).saveConfig();
    }
}
