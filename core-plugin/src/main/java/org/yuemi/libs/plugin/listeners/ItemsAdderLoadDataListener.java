package org.yuemi.libs.plugin.listeners;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.yuemi.libs.plugin.YueMiLibsPlugin;

/**
 * Listener to log when ItemsAdder finishes loading its custom items.
 */
public final class ItemsAdderLoadDataListener implements Listener {

    private final YueMiLibsPlugin plugin;

    public ItemsAdderLoadDataListener(YueMiLibsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemsAdderLoad(ItemsAdderLoadDataEvent event) {
        plugin.getLogger().info("ItemsAdder data loaded – item provider ready.");
    }
}
