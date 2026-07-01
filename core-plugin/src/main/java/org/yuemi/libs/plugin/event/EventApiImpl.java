package org.yuemi.libs.plugin.event;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.event.EventApi;
import org.yuemi.libs.api.event.EventProvider;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.bukkit.BukkitEvents;
import org.yuemi.libs.plugin.event.bukkit.BukkitEventsImpl;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concrete implementation of the EventApi interface.
 */
public final class EventApiImpl implements EventApi {

    private final BukkitEventsImpl bukkitEvents;
    private final Map<String, EventProvider> providers = new ConcurrentHashMap<>();
    private final Set<EventSubscription<?>> activeSubscriptions = ConcurrentHashMap.newKeySet();

    public EventApiImpl(@NotNull Plugin plugin) {
        this.bukkitEvents = new BukkitEventsImpl(plugin, this);
    }

    @Override
    public @NotNull BukkitEvents bukkit() {
        return bukkitEvents;
    }

    @Override
    public @Nullable EventProvider getProvider(@NotNull String namespace) {
        return providers.get(namespace.toLowerCase());
    }

    @Override
    public void registerProvider(@NotNull String namespace, @NotNull EventProvider provider) {
        providers.put(namespace.toLowerCase(), provider);
    }

    void trackSubscription(@NotNull EventSubscription<?> subscription) {
        activeSubscriptions.add(subscription);
    }

    void untrackSubscription(@NotNull EventSubscription<?> subscription) {
        activeSubscriptions.remove(subscription);
    }

    /**
     * Unregisters all active event subscriptions.
     */
    public void disable() {
        for (EventSubscription<?> subscription : activeSubscriptions) {
            subscription.unsubscribe();
        }
        activeSubscriptions.clear();
        providers.clear();
    }
}
