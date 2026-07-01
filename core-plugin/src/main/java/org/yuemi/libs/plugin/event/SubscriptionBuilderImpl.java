package org.yuemi.libs.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.SubscriptionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of SubscriptionBuilder using Bukkit's dynamic event registration.
 *
 * @param <T> the event type
 */
public final class SubscriptionBuilderImpl<T extends Event> implements SubscriptionBuilder<T> {

    private final Plugin plugin;
    private final Class<T> eventClass;
    private final EventApiImpl api;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled = false;
    private final List<Predicate<T>> filters = new ArrayList<>();

    public SubscriptionBuilderImpl(
            @NotNull Plugin plugin,
            @NotNull Class<T> eventClass,
            @NotNull EventApiImpl api) {
        this.plugin = plugin;
        this.eventClass = eventClass;
        this.api = api;
    }

    @Override
    public @NotNull SubscriptionBuilder<T> priority(@NotNull EventPriority priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public @NotNull SubscriptionBuilder<T> ignoreCancelled(boolean ignoreCancelled) {
        this.ignoreCancelled = ignoreCancelled;
        return this;
    }

    @Override
    public @NotNull SubscriptionBuilder<T> filter(@NotNull Predicate<T> filter) {
        this.filters.add(filter);
        return this;
    }

    @Override
    public @NotNull EventSubscription<T> handler(@NotNull Consumer<T> handler) {
        Listener listener = new Listener() {};

        EventExecutor executor = (l, event) -> {
            if (!eventClass.isInstance(event)) {
                return;
            }
            T castedEvent = eventClass.cast(event);
            for (Predicate<T> filter : filters) {
                if (!filter.test(castedEvent)) {
                    return;
                }
            }
            handler.accept(castedEvent);
        };

        plugin.getServer().getPluginManager().registerEvent(
                eventClass,
                listener,
                priority,
                executor,
                plugin,
                ignoreCancelled
        );

        EventSubscriptionImpl<T> subscription = new EventSubscriptionImpl<>(eventClass, listener, api);
        api.trackSubscription(subscription);

        return subscription;
    }
}
