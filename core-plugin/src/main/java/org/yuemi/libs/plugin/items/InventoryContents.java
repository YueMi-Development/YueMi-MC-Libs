package org.yuemi.libs.plugin.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Reads a player's inventory contents as an array of possibly-null slots.
 *
 * <p>Contents are read slot by slot through {@link PlayerInventory#getSize()} and
 * {@link PlayerInventory#getItem(int)} rather than {@code getContents()}, because
 * {@code getContents()} carries contradictory nullability annotations upstream: it is
 * {@code @Nullable} at declaration level but {@code @NotNull} on the method return type, with
 * {@code @Nullable} on the array elements. Analyzers read those two annotations inconsistently,
 * so the call cannot be used without a finding either way. The slot accessors are unambiguous.
 *
 * <p>The returned array is allocated here, so it is never null; individual slots may be null
 * when the corresponding slot is empty.
 */
final class InventoryContents {

    private InventoryContents() {
    }

    static @NotNull ItemStack[] of(@NotNull Player player) {
        PlayerInventory inventory = player.getInventory();
        int size = inventory.getSize();
        ItemStack[] contents = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            contents[i] = inventory.getItem(i);
        }
        return contents;
    }
}
