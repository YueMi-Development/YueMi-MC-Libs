package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

/**
 * Represents a single layer of items in a layered GUI layout.
 */
public interface GuiLayer {

    /**
     * Gets the unique ID of the layer.
     *
     * @return the layer ID
     */
    @NotNull
    String getId();

    /**
     * Gets the priority of this layer. Layers with higher priority values
     * override lower priority layers in slot conflicts.
     *
     * @return the layer priority
     */
    int getPriority();

    /**
     * Sets an interactive item in a specific slot on this layer.
     *
     * @param slot the inventory slot (0-indexed)
     * @param item the item, or null to clear the slot
     */
    void setItem(int slot, @Nullable GuiItem item);

    /**
     * Gets the interactive item in a specific slot on this layer.
     *
     * @param slot the inventory slot
     * @return the item, or null if empty
     */
    @Nullable
    GuiItem getItem(int slot);

    /**
     * Removes the item from a specific slot on this layer.
     *
     * @param slot the inventory slot
     */
    void removeItem(int slot);

    /**
     * Gets all items configured on this layer.
     *
     * @return a map of slot to GuiItem
     */
    @NotNull
    Map<Integer, GuiItem> getItems();
}
