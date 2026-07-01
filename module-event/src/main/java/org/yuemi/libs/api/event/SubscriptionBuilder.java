package org.yuemi.libs.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A builder for creating event subscriptions.
 *
 * @param <T> the event type
 */
public interface SubscriptionBuilder<T extends Event> {

    /**
     * Sets the priority of the event listener.
     *
     * @param priority the event priority
     * @return this builder
     */
    @NotNull
    SubscriptionBuilder<T> priority(@NotNull EventPriority priority);

    /**
     * Sets whether to ignore cancelled events.
     *
     * @param ignoreCancelled true to ignore cancelled events, false otherwise
     * @return this builder
     */
    @NotNull
    SubscriptionBuilder<T> ignoreCancelled(boolean ignoreCancelled);

    /**
     * Adds a filter to check before invoking the event handler.
     *
     * @param filter the event filter predicate
     * @return this builder
     */
    @NotNull
    SubscriptionBuilder<T> filter(@NotNull Predicate<T> filter);

    /**
     * Registers the event handler and activates the subscription.
     *
     * @param handler the event consumer
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<T> handler(@NotNull Consumer<T> handler);
}
