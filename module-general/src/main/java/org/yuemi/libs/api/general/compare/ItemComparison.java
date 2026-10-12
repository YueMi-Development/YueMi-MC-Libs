package org.yuemi.libs.api.general.compare;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the result of comparing two ItemStacks.
 */
public record ItemComparison(
    boolean areEqual,
    boolean similar,
    boolean sameMaterial,
    boolean sameAmount,
    boolean sameDurability,
    boolean sameMeta,
    boolean sameEnchantments,
    boolean sameCustomModelData,
    @Nullable String differenceReason
) {
    /**
     * Creates a comparison where items are equal.
     */
    public static ItemComparison equal() {
        return new ItemComparison(
            true, true, true, true, true, true, true, true, null
        );
    }

    /**
     * Creates a comparison where items are similar but not equal.
     */
    public static ItemComparison similar(@NotNull String reason) {
        return new ItemComparison(
            false, true, true, true, true, true, true, true, reason
        );
    }

    /**
     * Creates a comparison where items are different.
     */
    public static ItemComparison different(@NotNull String reason) {
        return new ItemComparison(
            false, false, false, false, false, false, false, false, reason
        );
    }
}
