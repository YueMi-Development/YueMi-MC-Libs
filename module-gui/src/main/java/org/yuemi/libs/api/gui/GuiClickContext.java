package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Context containing information about a GUI slot interaction click.
 */
public interface GuiClickContext {

    /**
     * Gets the player who triggered the click.
     *
     * @return the player
     */
    @NotNull
    Player getPlayer();

    /**
     * Gets the raw inventory slot index that was clicked.
     *
     * @return the slot index
     */
    int getSlot();

    /**
     * Gets the type of click performed.
     *
     * @return the click type
     */
    @NotNull
    ClickType getClickType();

    /**
     * Gets the underlying Bukkit inventory click event.
     *
     * @return the click event
     */
    @NotNull
    InventoryClickEvent getEvent();

    /**
     * Gets the GUI instance where this click occurred.
     *
     * @return the GUI
     */
    @NotNull
    Gui getGui();
}
