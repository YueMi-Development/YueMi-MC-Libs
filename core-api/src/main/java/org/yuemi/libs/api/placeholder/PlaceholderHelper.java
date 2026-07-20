package org.yuemi.libs.api.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public final class PlaceholderHelper {

    private PlaceholderHelper() {}

    /**
     * Registers a PlaceholderAPI expansion dynamically.
     *
     * @param plugin the registering plugin
     * @param identifier the placeholder identifier (e.g. "magicauction")
     * @param handler the placeholder handler function mapping player and params to a string
     */
    public static void register(
            @NotNull JavaPlugin plugin,
            @NotNull String identifier,
            @NotNull BiFunction<OfflinePlayer, String, String> handler) {
        
        if (org.bukkit.Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new DynamicExpansion(plugin, identifier, handler).register();
        }
    }

    private static final class DynamicExpansion extends PlaceholderExpansion {
        private final JavaPlugin plugin;
        private final String identifier;
        private final BiFunction<OfflinePlayer, String, String> handler;

        public DynamicExpansion(JavaPlugin plugin, String identifier, BiFunction<OfflinePlayer, String, String> handler) {
            this.plugin = plugin;
            this.identifier = identifier;
            this.handler = handler;
        }

        @Override
        public @NotNull String getIdentifier() {
            return identifier;
        }

        @Override
        public @NotNull String getAuthor() {
            return String.join(", ", plugin.getDescription().getAuthors());
        }

        @Override
        public @NotNull String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
            return handler.apply(player, params);
        }
    }
}
