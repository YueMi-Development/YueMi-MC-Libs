package org.yuemi.libs.plugin.bstats;

import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.YueMiLibsProvider;

import java.util.stream.Collectors;
import java.util.Set;

/**
 * Registers bStats custom charts for tracking provider usage.
 */
public final class ProviderCharts {

    private ProviderCharts() {
    }

    /**
     * Registers economy provider and item provider pie charts.
     *
     * @param registrar the Metrics instance to register charts with
     * @param plugin the plugin used for logging
     */
    public static void register(org.bstats.bukkit.Metrics registrar, JavaPlugin plugin) {
        YueMiLibsApi api = YueMiLibsProvider.getApi();

        if (api == null) {
            plugin.getLogger().warning("YueMiLibsApi not available — bStats charts will report 'none'");
        }

        if (api != null) {
            registrar.addCustomChart(new SimplePie("economy_provider", () -> {
                var provider = api.getEconomy().getActiveProvider();
                if (provider != null) {
                    return provider.getName().toLowerCase();
                }
                return "none";
            }));

            registrar.addCustomChart(new SimplePie("item_providers", () -> {
                Set<String> namespaces = api.getItems().getRegisteredNamespaces();
                if (!namespaces.isEmpty()) {
                    return namespaces.stream()
                        .sorted()
                        .collect(Collectors.joining(","));
                }
                return "none";
            }));
        } else {
            registrar.addCustomChart(new SimplePie("economy_provider", () -> "none"));
            registrar.addCustomChart(new SimplePie("item_providers", () -> "none"));
        }
    }
}
