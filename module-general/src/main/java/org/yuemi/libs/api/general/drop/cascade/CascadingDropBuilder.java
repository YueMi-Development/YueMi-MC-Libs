package org.yuemi.libs.api.general.drop.cascade;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.general.drop.DropItem;
import org.yuemi.libs.api.general.random.RandomApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder for a {@link CascadingDropSystem}.
 *
 * <p>Example usage (low-to-high — lowest rarity checked first):</p>
 * <pre>{@code
 * CascadingDropSystem<Material> system = CascadingDropBuilder.create(randomApi)
 *     .direction(CascadeDirection.LOW_TO_HIGH)
 *     .addRarity(0.30)                    // tier 0 (common)
 *     .withDrop(Material.DIRT, 1.0)
 *     .addRarity(0.10)                    // tier 1 (rare)
 *     .withDrop(Material.GOLD_INGOT, 0.7)
 *     .withDrop(Material.DIAMOND, 0.3)
 *     .mustHaveSome(List.of(Material.COAL))
 *     .build();
 * }</pre>
 *
 * @param <T> the type of items that can be dropped
 */
public class CascadingDropBuilder<T> {

    /**
     * Starts a new builder.
     *
     * @param randomApi the random source to use for all rolls
     * @param <T>       the drop item type
     * @return a new builder instance
     */
    public static <T> CascadingDropBuilder<T> create(@NotNull RandomApi randomApi) {
        return new CascadingDropBuilder<>(randomApi);
    }

    private final @NotNull RandomApi randomApi;
    private final @NotNull List<Double> rarityChances = new ArrayList<>();
    private final @NotNull List<@NotNull List<DropItem<T>>> rarityDrops = new ArrayList<>();

    private CascadeDirection direction = CascadeDirection.LOW_TO_HIGH;
    private List<@NotNull T> mustHaveSome = Collections.emptyList();

    private CascadingDropBuilder(@NotNull RandomApi randomApi) {
        this.randomApi = randomApi;
    }

    /**
     * Sets the direction in which rarity tiers are evaluated.
     * Defaults to {@link CascadeDirection#LOW_TO_HIGH}.
     *
     * @param direction the cascade direction
     * @return this builder
     */
    public @NotNull CascadingDropBuilder<T> direction(@NotNull CascadeDirection direction) {
        this.direction = direction;
        return this;
    }

    /**
     * Opens a new rarity tier with the given hit chance.
     * All subsequent {@link #withDrop(Object, double)} calls belong to this tier
     * until the next {@code addRarity} or {@link #build()} is called.
     *
     * @param chance the probability of hitting this rarity tier, in {@code [0.0, 1.0]}
     * @return this builder
     * @throws IllegalArgumentException if chance is outside {@code [0.0, 1.0]}
     */
    public @NotNull CascadingDropBuilder<T> addRarity(double chance) {
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException("chance must be in [0.0, 1.0], got: " + chance);
        }
        rarityChances.add(chance);
        rarityDrops.add(new ArrayList<>());
        return this;
    }

    /**
     * Adds a drop item to the currently open rarity tier.
     * Must be called after {@link #addRarity(double)}.
     *
     * @param item   the item to potentially drop
     * @param chance the relative chance within the tier, in {@code [0.0, 1.0]}
     * @return this builder
     * @throws IllegalStateException   if no rarity tier is open
     * @throws IllegalArgumentException if chance is outside {@code [0.0, 1.0]}
     */
    public @NotNull CascadingDropBuilder<T> withDrop(@NotNull T item, double chance) {
        if (rarityDrops.isEmpty()) {
            throw new IllegalStateException("Must call addRarity() before withDrop()");
        }
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException("chance must be in [0.0, 1.0], got: " + chance);
        }
        rarityDrops.get(rarityDrops.size() - 1).add(new DropItem<>(item, chance));
        return this;
    }

    /**
     * Sets a fallback list that is used when every rarity tier misses.
     * If set, the system is guaranteed to return a non-null value (uniform random pick
     * from the list). If not set, a missed system returns {@code null}.
     *
     * @param fallbacks the list of fallback items
     * @return this builder
     */
    public @NotNull CascadingDropBuilder<T> mustHaveSome(@NotNull List<@NotNull T> fallbacks) {
        this.mustHaveSome = List.copyOf(fallbacks);
        return this;
    }

    /**
     * Builds the {@link CascadingDropSystem}.
     *
     * @return a new drop system
     * @throws IllegalStateException if no drops have been configured
     */
    public @NotNull CascadingDropSystem<T> build() {
        return new CascadingDropSystem<>(
            randomApi,
            List.copyOf(rarityChances),
            List.copyOf(rarityDrops),
            List.copyOf(mustHaveSome),
            direction
        );
    }
}
