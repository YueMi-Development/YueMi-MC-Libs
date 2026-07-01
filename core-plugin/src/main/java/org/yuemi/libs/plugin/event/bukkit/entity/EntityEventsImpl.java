package org.yuemi.libs.plugin.event.bukkit.entity;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import org.yuemi.libs.api.event.bukkit.entity.EntityEvents;
import org.yuemi.libs.plugin.event.bukkit.BukkitEventsImpl;
import java.util.function.Consumer;

/**
 * Implementation of EntityEvents.
 */
public final class EntityEventsImpl implements EntityEvents {

    private final BukkitEventsImpl parent;

    public EntityEventsImpl(@NotNull BukkitEventsImpl parent) {
        this.parent = parent;
    }

    @Override
    public @NotNull EventSubscription<EntityDamageEvent> damage(@NotNull Consumer<EntityDamageEvent> handler) {
        return parent.subscribe(EntityDamageEvent.class).handler(handler);
    }

    @Override
    public @NotNull EventSubscription<EntityDeathEvent> death(@NotNull Consumer<EntityDeathEvent> handler) {
        return parent.subscribe(EntityDeathEvent.class).handler(handler);
    }
}
