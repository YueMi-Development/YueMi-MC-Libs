package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Entry point for creating GUI instances.
 */
public interface GuiApi {

    /**
     * Creates a new fluent GUI builder.
     *
     * @return a new GUI builder
     */
    @NotNull
    GuiBuilder createBuilder();

    /**
     * Creates a new fluent GUI Item builder.
     *
     * @return a new GUI Item builder
     */
    @NotNull
    GuiItem.Builder createItemBuilder();

    /**
     * Creates a new fluent Anvil GUI builder for text input.
     *
     * @return a new Anvil GUI builder
     */
    @NotNull
    AnvilInputBuilder createAnvilInputBuilder();

    /**
     * Creates a new fluent Sign GUI builder for text input.
     *
     * @return a new Sign GUI builder
     */
    @NotNull
    SignInputBuilder createSignInputBuilder();

    /**
     * Registers slot positions that accept items being placed into them.
     * The click handler uses this registry to distinguish {@code PLACE} from
     * {@code SWAP} when a slot already contains a placeholder/rendered item —
     * required for any input-style slot whose placeholder would otherwise be
     * read as "slot is occupied" by the inventory.
     *
     * <p>Slots typically also declare {@link SlotBehavior#input()} on their
     * {@link GuiItem}, but this registry covers the legacy/edge-case path
     * where items have no behavior set.
     *
     * @param positions the slot positions that accept placing items
     */
    void registerInputSlots(@NotNull Collection<Integer> positions);

    /**
     * Removes the given positions from the input slot registry. Useful when
     * a GUI is rebuilt/closed and the old positions should stop being treated
     * as inputs.
     *
     * @param positions the slot positions to deregister
     */
    void unregisterInputSlots(@NotNull Collection<Integer> positions);
}
