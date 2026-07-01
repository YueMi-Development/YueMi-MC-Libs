package org.yuemi.libs.api.event;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an active subscription to a Bukkit event.
 *
 * @param <T> the event type
 */
public interface EventSubscription<T extends Event> {

    /**
     * Gets the class of the event being listened to.
     *
     * @return the event class
     */
    @NotNull
    Class<T> getEventClass();

    /**
     * Unregisters the event handler.
     */
    void unsubscribe();

    /**
     * Checks if the subscription is currently active.
     *
     * @return true if active, false otherwise
     */
    boolean isActive();
}
