package org.yuemi.libs.api.event.bukkit.entity;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.event.EventSubscription;
import java.util.function.Consumer;

/**
 * Entity-related event provider.
 */
public interface EntityEvents {

    /**
     * Shorthand to listen to entity damage events.
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<EntityDamageEvent> damage(@NotNull Consumer<EntityDamageEvent> handler);

    /**
     * Shorthand to listen to entity death events.
     *
     * @param handler the event handler
     * @return the active event subscription
     */
    @NotNull
    EventSubscription<EntityDeathEvent> death(@NotNull Consumer<EntityDeathEvent> handler);
}
