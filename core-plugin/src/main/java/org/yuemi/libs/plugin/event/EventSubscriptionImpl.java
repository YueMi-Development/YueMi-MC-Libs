package org.yuemi.libs.plugin.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;

/**
 * Implementation of EventSubscription representing an active event registration.
 *
 * @param <T> the event type
 */
public final class EventSubscriptionImpl<T extends Event> implements EventSubscription<T> {

    private final Class<T> eventClass;
    private final Listener listener;
    private final EventApiImpl api;
    private boolean active = true;

    public EventSubscriptionImpl(
            @NotNull Class<T> eventClass,
            @NotNull Listener listener,
            @NotNull EventApiImpl api) {
        this.eventClass = eventClass;
        this.listener = listener;
        this.api = api;
    }

    @Override
    public @NotNull Class<T> getEventClass() {
        return eventClass;
    }

    @Override
    public void unsubscribe() {
        if (active) {
            HandlerList.unregisterAll(listener);
            active = false;
            api.untrackSubscription(this);
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
