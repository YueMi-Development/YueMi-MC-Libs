package org.yuemi.libs.plugin.items;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.items.ItemProvider;
import java.util.Map;

/**
 * ItemProvider implementation that bridges Nexo custom items to the YueMi Libs Items API.
 */
public final class NexoItemProvider implements ItemProvider {

    @Override
    public @NotNull String getName() {
        return "Nexo";
    }

    @Override
    public boolean isAvailable() {
        return Bukkit.getPluginManager().isPluginEnabled("Nexo");
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull String id, int amount) {
        if (!isAvailable()) {
            return null;
        }
        ItemBuilder builder = NexoItems.itemFromId(id);
        if (builder == null) {
            return null;
        }
        ItemStack item = builder.build();
        if (item != null) {
            item.setAmount(amount);
        }
        return item;
    }

    private boolean matches(@NotNull ItemStack item, @NotNull String id) {
        String nexoId = NexoItems.idFromItem(item);
        if (nexoId == null) {
            return false;
        }
        return nexoId.equalsIgnoreCase(id);
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
