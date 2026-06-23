package org.yuemi.libs.api.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a specific item provider (e.g. Vanilla, Nexo, ItemsAdder, CraftEngine, etc.).
 */
public interface ItemProvider {

    /**
     * Gets the unique name of this item provider.
     *
     * @return the provider name
     */
    @NotNull
    String getName();

    /**
     * Checks if this item provider is available.
     *
     * @return true if available, false otherwise
     */
    boolean isAvailable();

    /**
     * Gets an item stack by its ID.
     *
     * @param id the item ID
     * @param amount the item quantity
     * @return the item stack, or null if not found
     */
    @Nullable
    ItemStack getItem(@NotNull String id, int amount);

    /**
     * Gives an item to the player.
     *
     * @param player the player
     * @param id the item ID
     * @param amount the item quantity
     * @return true if successful, false otherwise
     */
    boolean giveItem(@NotNull Player player, @NotNull String id, int amount);

    /**
     * Takes an item from the player's inventory.
     *
     * @param player the player
     * @param id the item ID
     * @param amount the item quantity to take
     * @return true if successful, false otherwise
     */
    boolean takeItem(@NotNull Player player, @NotNull String id, int amount);

    /**
     * Gets the count of matching items in the player's inventory.
     *
     * @param player the player
     * @param id the item ID
     * @return the number of matching items in player's inventory
     */
    int getItemCount(@NotNull Player player, @NotNull String id);
}
