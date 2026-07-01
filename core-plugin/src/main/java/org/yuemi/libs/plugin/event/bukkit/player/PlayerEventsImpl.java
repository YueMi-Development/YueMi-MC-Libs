package org.yuemi.libs.plugin.event.bukkit.player;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.bukkit.player.PlayerEvents;
import org.yuemi.libs.plugin.event.bukkit.BukkitEventsImpl;
import java.util.function.Consumer;

/**
 * Implementation of PlayerEvents.
 */
public final class PlayerEventsImpl implements PlayerEvents {

    private final BukkitEventsImpl parent;

    public PlayerEventsImpl(@NotNull BukkitEventsImpl parent) {
        this.parent = parent;
    }

    @Override
    public @NotNull EventSubscription<PlayerJoinEvent> join(@NotNull Consumer<PlayerJoinEvent> handler) {
        return parent.subscribe(PlayerJoinEvent.class).handler(handler);
    }

    @Override
    public @NotNull EventSubscription<PlayerInteractEvent> rightClick(@NotNull Consumer<PlayerInteractEvent> handler) {
        return parent.subscribe(PlayerInteractEvent.class)
                .filter(event -> event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                .handler(handler);
    }

    @Override
    public @NotNull EventSubscription<PlayerInteractEvent> leftClick(@NotNull Consumer<PlayerInteractEvent> handler) {
        return parent.subscribe(PlayerInteractEvent.class)
                .filter(event -> event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                .handler(handler);
    }
}
