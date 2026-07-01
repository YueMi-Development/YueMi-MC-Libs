package org.yuemi.libs.api.event.bukkit;

import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventProvider;
import org.yuemi.libs.api.event.SubscriptionBuilder;
import org.yuemi.libs.api.event.bukkit.block.BlockEvents;
import org.yuemi.libs.api.event.bukkit.entity.EntityEvents;
import org.yuemi.libs.api.event.bukkit.player.PlayerEvents;

/**
 * Event registration provider for standard Bukkit/Vanilla events.
 */
public interface BukkitEvents extends EventProvider {

    /**
     * Starts building an event subscription.
     *
     * @param eventClass the class of the event
     * @param <T> the event type
     * @return a subscription builder
     */
    @NotNull
    <T extends Event> SubscriptionBuilder<T> subscribe(@NotNull Class<T> eventClass);

    /**
     * Gets the player-related event provider.
     *
     * @return the player events provider
     */
    @NotNull
    PlayerEvents player();

    /**
     * Gets the block-related event provider.
     *
     * @return the block events provider
     */
    @NotNull
    BlockEvents block();

    /**
     * Gets the entity-related event provider.
     *
     * @return the entity events provider
     */
    @NotNull
    EntityEvents entity();
}
