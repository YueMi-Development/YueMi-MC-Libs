package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;

/**
 * Fluent builder for creating GuiLayer instances.
 */
public interface GuiLayerBuilder {

    /**
     * Sets an item at a specific slot in the layer.
     *
     * @param slot the slot index
     * @param item the GUI item
     * @return this builder
     */
    @NotNull
    GuiLayerBuilder setItem(int slot, @NotNull GuiItem item);

    /**
     * Fills the entire layer inventory with the specified item.
     * Useful for background fills.
     *
     * @param item the GUI item
     * @return this builder
     */
    @NotNull
    GuiLayerBuilder fill(@NotNull GuiItem item);

    /**
     * Builds and returns the final GuiLayer instance.
     *
     * @return the constructed GuiLayer
     */
    @NotNull
    GuiLayer build();
}
