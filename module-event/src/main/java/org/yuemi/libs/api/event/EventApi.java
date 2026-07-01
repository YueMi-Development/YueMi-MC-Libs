package org.yuemi.libs.api.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.event.bukkit.BukkitEvents;

/**
 * API for registering event handlers dynamically, organized by providers.
 */
public interface EventApi {

    /**
     * Gets the Bukkit/Vanilla event registration provider.
     *
     * @return the Bukkit events provider
     */
    @NotNull
    BukkitEvents bukkit();

    /**
     * Gets a registered event provider by namespace (e.g. "mobs", "items").
     *
     * @param namespace the namespace of the provider
     * @return the event provider, or null if not registered
     */
    @Nullable
    EventProvider getProvider(@NotNull String namespace);

    /**
     * Registers a custom event provider under a namespace.
     *
     * @param namespace the namespace of the provider
     * @param provider the event provider
     */
    void registerProvider(@NotNull String namespace, @NotNull EventProvider provider);
}
