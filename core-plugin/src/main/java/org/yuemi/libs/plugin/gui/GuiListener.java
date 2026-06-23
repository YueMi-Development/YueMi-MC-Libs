package org.yuemi.libs.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public final class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        InventoryHolder holder = topInventory.getHolder();

        if (!(holder instanceof GuiHolder guiHolder)) {
            return;
        }

        // Cancel all clicks when a GUI is open to prevent stealing/modifying items
        event.setCancelled(true);

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !(clickedInventory.getHolder() instanceof GuiHolder)) {
            return;
        }

        int slot = event.getSlot();
        if (slot < 0 || slot >= clickedInventory.getSize()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Gui gui = guiHolder.getGui();

        // Resolve which item is active in this slot (from highest priority layer to lowest)
        Collection<GuiLayer> layers = gui.getLayers();
        List<GuiLayer> descendingLayers = new ArrayList<>(layers);
        // Sort descending by priority manually
        descendingLayers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        GuiItem clickedItem = null;
        for (GuiLayer layer : descendingLayers) {
            GuiItem item = layer.getItem(slot);
            if (item != null && item.shouldRender(player)) {
                clickedItem = item;
                break;
            }
        }

        if (clickedItem != null) {
            GuiClickContextImpl context = new GuiClickContextImpl(
                    player,
                    slot,
                    event.getClick(),
                    event,
                    gui
            );
            clickedItem.onClick(context);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (holder instanceof GuiHolder) {
            event.setCancelled(true);
        }
    }
}
