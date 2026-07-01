package org.yuemi.libs.plugin.event.bukkit;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.bukkit.BukkitEvents;
import org.yuemi.libs.api.event.bukkit.block.BlockEvents;
import org.yuemi.libs.api.event.bukkit.entity.EntityEvents;
import org.yuemi.libs.api.event.bukkit.player.PlayerEvents;
import org.yuemi.libs.api.event.SubscriptionBuilder;
import org.yuemi.libs.plugin.event.EventApiImpl;
import org.yuemi.libs.plugin.event.SubscriptionBuilderImpl;
import org.yuemi.libs.plugin.event.bukkit.block.BlockEventsImpl;
import org.yuemi.libs.plugin.event.bukkit.entity.EntityEventsImpl;
import org.yuemi.libs.plugin.event.bukkit.player.PlayerEventsImpl;

/**
 * Implementation of BukkitEvents for registering standard Bukkit/Vanilla events.
 */
public final class BukkitEventsImpl implements BukkitEvents {

    private final Plugin plugin;
    private final EventApiImpl api;
    private final PlayerEvents playerEvents;
    private final BlockEvents blockEvents;
    private final EntityEvents entityEvents;

    public BukkitEventsImpl(@NotNull Plugin plugin, @NotNull EventApiImpl api) {
        this.plugin = plugin;
        this.api = api;
        this.playerEvents = new PlayerEventsImpl(this);
        this.blockEvents = new BlockEventsImpl(this);
        this.entityEvents = new EntityEventsImpl(this);
    }

    @Override
    public @NotNull <T extends Event> SubscriptionBuilder<T> subscribe(@NotNull Class<T> eventClass) {
        return new SubscriptionBuilderImpl<>(plugin, eventClass, api);
    }

    @Override
    public @NotNull PlayerEvents player() {
        return playerEvents;
    }

    @Override
    public @NotNull BlockEvents block() {
        return blockEvents;
    }

    @Override
    public @NotNull EntityEvents entity() {
        return entityEvents;
    }
}
