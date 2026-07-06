package org.yuemi.libs.plugin.items;

import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitItemDefinition;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.items.ItemProvider;
import java.util.Map;

public final class CraftEngineItemProvider implements ItemProvider {

    @Override
    public @NotNull String getName() {
        return "CraftEngine";
    }

    @Override
    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("CraftEngine");
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull String id, int amount) {
        if (!isAvailable()) {
            return null;
        }
        // Safely check if CraftEngine's item manager / registry is initialized via reflection
        try {
            Class<?> clazz = Class.forName("net.momirealms.craftengine.bukkit.item.BukkitItemManager");
            java.lang.reflect.Method instanceMethod = clazz.getMethod("instance");
            if (instanceMethod.invoke(null) == null) {
                return null;
            }
        } catch (Throwable ignored) {
            return null; // Return null if CraftEngine is not initialized or class/method is not present
        }

        // Clean up both possible prefixes
        String cleanId = id;
        if (cleanId.startsWith("craftengine:")) {
            cleanId = cleanId.substring("craftengine:".length());
        }

        // Try getting by the cleaned ID directly
        BukkitItemDefinition definition = CraftEngineItems.byId(Key.from(cleanId));
        if (definition == null && !cleanId.contains(":")) {
            // Fall back to default: prefix if it has no namespace
            definition = CraftEngineItems.byId(Key.from("default:" + cleanId));
        }

        if (definition == null) {
            return null;
        }
        ItemStack item = definition.buildBukkitItem();
        if (item != null) {
            item.setAmount(amount);
        }
        return item;
    }

    private boolean matches(@NotNull ItemStack item, @NotNull String id) {
        if (!CraftEngineItems.isCustomItem(item)) {
            return false;
        }
        Key itemId = CraftEngineItems.getCustomItemId(item);
        if (itemId == null) {
            return false;
        }
        // If the query ID starts with craftengine:, we check it against the Key's value or default:value since they are equivalent
        String cleanId = id.startsWith("craftengine:") ? id.substring("craftengine:".length()) : id;
        String itemNamespace = itemId.namespace();
        String itemValue = itemId.value();

        if (cleanId.contains(":")) {
            return itemId.asString().equalsIgnoreCase(cleanId) || (itemNamespace.equalsIgnoreCase("default") && ("default:" + itemValue).equalsIgnoreCase(cleanId));
        } else {
            return itemValue.equalsIgnoreCase(cleanId) || itemId.asString().equalsIgnoreCase(cleanId) || (itemNamespace.equalsIgnoreCase("default") && ("default:" + itemValue).equalsIgnoreCase(cleanId));
        }
    }

    @Override
    public boolean giveItem(@NotNull Player player, @NotNull String id, int amount) {
        ItemStack item = getItem(id, amount);
        if (item == null) {
            return false;
        }
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        if (!leftovers.isEmpty()) {
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
        return true;
    }

    @Override
    public boolean takeItem(@NotNull Player player, @NotNull String id, int amount) {
        if (amount <= 0) {
            return true;
        }
        int totalCount = getItemCount(player, id);
        if (totalCount < amount) {
            return false;
        }
        int remainingToTake = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && matches(item, id)) {
                int count = item.getAmount();
                if (count <= remainingToTake) {
                    remainingToTake -= count;
                    player.getInventory().setItem(i, null);
                } else {
                    item.setAmount(count - remainingToTake);
                    remainingToTake = 0;
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public int getItemCount(@NotNull Player player, @NotNull String id) {
        if (!isAvailable()) {
            return 0;
        }
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && matches(item, id)) {
                count += item.getAmount();
            }
        }
        return count;
    }
}
