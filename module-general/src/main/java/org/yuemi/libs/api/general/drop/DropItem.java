package org.yuemi.libs.api.general.drop;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a single drop entry within a rarity tier of a cascading drop system.
 *
 * @param <T> the type of the dropped item
 */
public record DropItem<T>(
    @NotNull T item,
    double chance
) {
    /**
     * Creates a new drop item.
     *
     * @param item   the dropped object (e.g. {@code Material}, {@code ItemStack})
     * @param chance the chance of this specific item being chosen, in {@code [0.0, 1.0]}.
     *               All items within the same rarity tier should have chances that sum to
     *               {@code <= 1.0}; the remainder represents a "no drop" within the tier.
     * @throws IllegalArgumentException if chance is outside {@code [0.0, 1.0]}
     */
    public DropItem {
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException("chance must be in [0.0, 1.0], got: " + chance);
        }
        if (item == null) {
            throw new IllegalArgumentException("item must not be null");
        }
    }
}
