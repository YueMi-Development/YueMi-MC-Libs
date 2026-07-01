package org.yuemi.libs.plugin.event.bukkit.block;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.bukkit.block.BlockEvents;
import org.yuemi.libs.plugin.event.bukkit.BukkitEventsImpl;
import java.util.function.Consumer;

/**
 * Implementation of BlockEvents.
 */
public final class BlockEventsImpl implements BlockEvents {

    private final BukkitEventsImpl parent;

    public BlockEventsImpl(@NotNull BukkitEventsImpl parent) {
        this.parent = parent;
    }

    @Override
    public @NotNull EventSubscription<BlockBreakEvent> breakBlock(@NotNull Consumer<BlockBreakEvent> handler) {
        return parent.subscribe(BlockBreakEvent.class).handler(handler);
    }

    @Override
    public @NotNull EventSubscription<BlockPlaceEvent> placeBlock(@NotNull Consumer<BlockPlaceEvent> handler) {
        return parent.subscribe(BlockPlaceEvent.class).handler(handler);
    }
}
