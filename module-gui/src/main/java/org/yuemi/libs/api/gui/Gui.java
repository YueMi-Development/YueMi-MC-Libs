package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

/**
 * Represents an interactive user interface inventory.
 */
public interface Gui {

    /**
     * Gets the title of the GUI.
     *
     * @return the title
     */
    @NotNull
    String getTitle();

    /**
     * Gets the number of rows in the GUI.
     *
     * @return the number of rows (1 to 6)
     */
    int getRows();

    /**
     * Adds a layout layer to the GUI.
     *
     * @param layer the layer to add
     */
    void addLayer(@NotNull GuiLayer layer);

    /**
     * Removes a layer from the GUI by its unique ID.
     *
     * @param layerId the ID of the layer to remove
     */
    void removeLayer(@NotNull String layerId);

    /**
     * Gets all layers registered in this GUI, ordered by priority.
     *
     * @return a collection of layers
     */
    @NotNull
    Collection<GuiLayer> getLayers();

    /**
     * Opens this GUI for the specified player.
     *
     * @param player the player to open the GUI for
     */
    void open(@NotNull Player player);

    /**
     * Re-renders the GUI for the player by re-evaluating conditions and layers.
     *
     * @param player the player to update the GUI for
     */
    void update(@NotNull Player player);
}
