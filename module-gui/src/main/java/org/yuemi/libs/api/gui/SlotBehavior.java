package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;

/**
 * Per-slot interaction behavior configuration.
 * Controls which player actions are allowed on a specific GUI slot.
 */
public record SlotBehavior(
    boolean allowPickup,
    boolean allowPlace,
    boolean allowShift,
    boolean allowDrag
) {
    /**
     * Behavior that allows all interactions (default).
     */
    public static final SlotBehavior ALL = new SlotBehavior(true, true, true, true);

    /**
     * Behavior that allows no interactions (display-only).
     */
    public static final SlotBehavior NONE = new SlotBehavior(false, false, false, false);

    /**
     * Behavior for static display slots - cannot pick up or place items.
     */
    public static final SlotBehavior DISPLAY = new SlotBehavior(false, false, false, false);

    /**
     * Behavior for container slots - can place items in and pick items out.
     */
    public static final SlotBehavior CONTAINER = new SlotBehavior(true, true, true, true);

    /**
     * Behavior for read-only display - cannot be modified.
     */
    public static final SlotBehavior READ_ONLY = new SlotBehavior(false, false, false, false);

    /**
     * Behavior for an "output" slot - items can be taken but not placed.
     */
    public static SlotBehavior output() {
        return new SlotBehavior(true, false, false, false);
    }

    /**
     * Behavior for an "input" slot - items can be placed but not taken.
     */
    public static SlotBehavior input() {
        return new SlotBehavior(false, true, true, true);
    }

    /**
     * Custom behavior builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private boolean allowPickup = true;
        private boolean allowPlace = true;
        private boolean allowShift = true;
        private boolean allowDrag = true;

        public Builder allowPickup(boolean v) { this.allowPickup = v; return this; }
        public Builder allowPlace(boolean v) { this.allowPlace = v; return this; }
        public Builder allowShift(boolean v) { this.allowShift = v; return this; }
        public Builder allowDrag(boolean v) { this.allowDrag = v; return this; }

        public @NotNull SlotBehavior build() {
            return new SlotBehavior(allowPickup, allowPlace, allowShift, allowDrag);
        }
    }
}
