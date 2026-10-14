package org.yuemi.libs.plugin.bstats;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.YueMiLibsProvider;
import org.yuemi.libs.api.items.ItemsApi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class BStatsService {

    private BStatsService() {
    }

    public static void initialize(JavaPlugin plugin) {
        try (InputStream input = plugin.getResource("plugin.yml")) {
            if (input == null) {
                return;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(input, StandardCharsets.UTF_8));
            String rawId = config.getString("bstats-id");
            if (rawId == null || rawId.trim().isEmpty() || rawId.contains("bstatsPluginId")) {
                return;
            }
            int pluginId = Integer.parseInt(rawId.trim());
            Metrics metrics = new Metrics(plugin, pluginId);
            plugin.getLogger().info("bStats metrics initialized successfully with ID: " + pluginId);

            ProviderCharts.register(metrics, plugin);

        } catch (NumberFormatException e) {
            plugin.getLogger().warning("Invalid bStats plugin ID in plugin.yml");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize bStats metrics", e);
        }
    }
}
