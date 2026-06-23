package org.yuemi.libs.api.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to manage item providers and query items using NAMESPACE:ID keys.
 */
public interface ItemsApi {

    /**
     * Registers a provider under a specific namespace.
     *
     * @param namespace the namespace (e.g. "minecraft")
     * @param provider the item provider
     */
    void registerProvider(@NotNull String namespace, @NotNull ItemProvider provider);

    /**
     * Gets an item stack by its namespace and ID key (e.g. "minecraft:stone").
     *
     * @param key the namespace:id key
     * @param amount the item quantity
     * @return the item stack, or null if provider/item is not found
     */
    @Nullable
    ItemStack getItem(@NotNull String key, int amount);

    /**
     * Gives an item by namespace and ID key to the player.
     *
     * @param player the player
     * @param key the namespace:id key
     * @param amount the item quantity
     * @return true if successful, false otherwise
     */
    boolean giveItem(@NotNull Player player, @NotNull String key, int amount);

    /**
     * Takes an item by namespace and ID key from the player.
     *
     * @param player the player
     * @param key the namespace:id key
     * @param amount the item quantity to take
     * @return true if successful, false otherwise
     */
    boolean takeItem(@NotNull Player player, @NotNull String key, int amount);

    /**
     * Gets the count of matching items by namespace and ID key in the player's inventory.
     *
     * @param player the player
     * @param key the namespace:id key
     * @return the count of matching items
     */
    int getItemCount(@NotNull Player player, @NotNull String key);
}
