package org.yuemi.libs.api.event.bukkit.player;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import java.util.function.Consumer;

/**
 * Player-related event provider.
 */
public interface PlayerEvents {

    /**
     * Shorthand to listen to player join events.
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<PlayerJoinEvent> join(@NotNull Consumer<PlayerJoinEvent> handler);

    /**
     * Shorthand to listen to player right-click actions (blocks or air).
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<PlayerInteractEvent> rightClick(@NotNull Consumer<PlayerInteractEvent> handler);

    /**
     * Shorthand to listen to player left-click actions (blocks or air).
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<PlayerInteractEvent> leftClick(@NotNull Consumer<PlayerInteractEvent> handler);
}
