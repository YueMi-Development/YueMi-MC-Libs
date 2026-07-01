package org.yuemi.libs.api.event.bukkit.block;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import java.util.function.Consumer;

/**
 * Block-related event provider.
 */
public interface BlockEvents {

    /**
     * Shorthand to listen to block break events.
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<BlockBreakEvent> breakBlock(@NotNull Consumer<BlockBreakEvent> handler);

    /**
     * Shorthand to listen to block place events.
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<BlockPlaceEvent> placeBlock(@NotNull Consumer<BlockPlaceEvent> handler);
}
