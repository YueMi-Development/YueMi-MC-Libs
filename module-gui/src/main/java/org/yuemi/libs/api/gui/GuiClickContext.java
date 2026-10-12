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
     * The semantic action that triggered this click context.
     */
    enum Action {
        /** Standard left click on a slot */
        LEFT_CLICK,
        /** Right click on a slot */
        RIGHT_CLICK,
        /** Shift-click (move item to other inventory) */
        SHIFT_CLICK,
        /** Player picks up an item from a slot */
        PICKUP,
        /** Player places an item from cursor into a slot */
        PLACE,
        /** Item is being placed via drag across slots */
        DRAG_PLACE,
        /** Item is being swapped with hotbar key */
        SWAP,
        /** Item is being dropped from cursor (Q key) */
        DROP_CURSOR
    }

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

    /**
     * Gets the semantic action that triggered this context.
     *
     * @return the action
     */
    @NotNull
    Action getAction();

    /**
     * Gets the item being placed or interacted with (from cursor, drag, or clicked slot).
     * May be null for pure PICKUP actions.
     *
     * @return the item, or null
     */
    org.bukkit.inventory.ItemStack getInteractedItem();
}
