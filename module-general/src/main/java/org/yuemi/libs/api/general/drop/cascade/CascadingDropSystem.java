package org.yuemi.libs.api.general.drop.cascade;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.general.drop.DropItem;
import org.yuemi.libs.api.general.random.RandomApi;

import java.util.List;
import java.util.Objects;

/**
 * A cascading rarity drop system.
 *
 * <p>Tiers are evaluated in the configured {@link CascadeDirection}.
 * When a tier is hit, a second roll picks among the items configured for that tier.
 * If every tier misses, an optional fallback list ({@code mustHaveSome}) is used;
 * if no fallback is configured, {@link #roll()} returns {@code null}.
 *
 * @param <T> the type of items that can be dropped
 */
public final class CascadingDropSystem<T> {

    private final @NotNull RandomApi randomApi;
    private final @NotNull List<Double> rarityChances;
    private final @NotNull List<@NotNull List<DropItem<T>>> rarityDrops;
    private final @NotNull List<@NotNull T> fallbacks;
    private final @NotNull CascadeDirection direction;

    CascadingDropSystem(
        @NotNull RandomApi randomApi,
        @NotNull List<Double> rarityChances,
        @NotNull List<@NotNull List<DropItem<T>>> rarityDrops,
        @NotNull List<@NotNull T> fallbacks,
        @NotNull CascadeDirection direction
    ) {
        this.randomApi = Objects.requireNonNull(randomApi, "randomApi");
        this.rarityChances = List.copyOf(rarityChances);
        this.rarityDrops = List.copyOf(rarityDrops);
        this.fallbacks = List.copyOf(fallbacks);
        this.direction = Objects.requireNonNull(direction, "direction");
    }

    /**
     * Performs a drop roll.
     *
     * @return a dropped item, a fallback item if all tiers missed and one is configured,
     *         or {@code null} if the entire cascade failed with no fallback.
     */
    public @Nullable T roll() {
        int[] order = direction == CascadeDirection.HIGH_TO_LOW
            ? new int[]{rarityChances.size() - 1, -1, -1}   // start, end (exclusive), step
            : new int[]{0, rarityChances.size(), 1};          // start, end (exclusive), step

        for (int i = order[0]; i != order[1]; i += order[2]) {
            if (randomApi.chance(rarityChances.get(i))) {
                T item = rollItemInTier(i);
                if (item != null) {
                    return item;
                }
            }
        }

        if (!fallbacks.isEmpty()) {
            return randomApi.pick(fallbacks);
        }

        return null;
    }

    /**
     * Rolls for an item within a specific rarity tier.
     *
     * @param tierIndex the tier index (0 = lowest rarity)
     * @return a randomly selected item from the tier, or {@code null} if no item won the roll
     */
    private @Nullable T rollItemInTier(int tierIndex) {
        List<DropItem<T>> drops = rarityDrops.get(tierIndex);
        if (drops.isEmpty()) {
            return null;
        }

        double roll = randomApi.nextDouble();
        double cumulative = 0.0;

        for (DropItem<T> drop : drops) {
            cumulative += drop.chance();
            if (roll < cumulative) {
                return drop.item();
            }
        }

        // No item selected (cumulative < 1.0)
        return null;
    }

    /**
     * Returns the number of configured rarity tiers.
     *
     * @return the tier count
     */
    public int rarityCount() {
        return rarityChances.size();
    }

    /**
     * Returns the unmodifiable list of drop items for a given rarity tier.
     *
     * @param rarityIndex the tier index (0 = lowest rarity)
     * @return the list of drops for that tier
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public @NotNull List<@NotNull DropItem<T>> dropsForRarity(int rarityIndex) {
        return List.copyOf(rarityDrops.get(rarityIndex));
    }

    /**
     * Returns the unmodifiable list of configured fallback items.
     *
     * @return the fallback list (may be empty)
     */
    public @NotNull List<@NotNull T> fallbacks() {
        return fallbacks;
    }

    /**
     * Returns the cascade direction.
     *
     * @return the direction
     */
    public @NotNull CascadeDirection direction() {
        return direction;
    }
}
