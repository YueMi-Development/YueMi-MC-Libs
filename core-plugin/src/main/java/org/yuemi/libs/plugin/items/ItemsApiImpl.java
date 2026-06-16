package org.yuemi.libs.plugin.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.items.ItemProvider;
import org.yuemi.libs.api.items.ItemsApi;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemsApiImpl implements ItemsApi {

    private final Map<String, ItemProvider> providers = new ConcurrentHashMap<>();

    private @Nullable ItemProvider getProviderForNamespace(@NotNull String namespace) {
        return providers.get(namespace.toLowerCase());
    }

    private static class ParsedKey {
        final String namespace;
        final String id;

        ParsedKey(String namespace, String id) {
            this.namespace = namespace;
            this.id = id;
        }
    }

    private ParsedKey parseKey(@NotNull String key) {
        int colonIdx = key.indexOf(':');
        if (colonIdx == -1) {
            return new ParsedKey("minecraft", key);
        }
        String namespace = key.substring(0, colonIdx).toLowerCase();
        String id = key.substring(colonIdx + 1);
        return new ParsedKey(namespace, id);
    }

    @Override
    public void registerProvider(@NotNull String namespace, @NotNull ItemProvider provider) {
        providers.put(namespace.toLowerCase(), provider);
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull String key, int amount) {
        ParsedKey parsed = parseKey(key);
        ItemProvider provider = getProviderForNamespace(parsed.namespace);
        if (provider == null || !provider.isAvailable()) {
            return null;
        }
        return provider.getItem(parsed.id, amount);
    }

    @Override
    public boolean giveItem(@NotNull Player player, @NotNull String key, int amount) {
        ParsedKey parsed = parseKey(key);
        ItemProvider provider = getProviderForNamespace(parsed.namespace);
        if (provider == null || !provider.isAvailable()) {
            return false;
        }
        return provider.giveItem(player, parsed.id, amount);
    }

    @Override
    public boolean takeItem(@NotNull Player player, @NotNull String key, int amount) {
        ParsedKey parsed = parseKey(key);
        ItemProvider provider = getProviderForNamespace(parsed.namespace);
        if (provider == null || !provider.isAvailable()) {
            return false;
        }
        return provider.takeItem(player, parsed.id, amount);
    }

    @Override
    public int getItemCount(@NotNull Player player, @NotNull String key) {
        ParsedKey parsed = parseKey(key);
        ItemProvider provider = getProviderForNamespace(parsed.namespace);
        if (provider == null || !provider.isAvailable()) {
            return 0;
        }
        return provider.getItemCount(player, parsed.id);
    }
}
