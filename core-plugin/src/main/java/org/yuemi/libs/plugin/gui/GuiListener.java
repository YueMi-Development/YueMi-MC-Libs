package org.yuemi.libs.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class GuiListener implements Listener {

    private final Set<UUID> anvilSubmitting = Collections.synchronizedSet(new HashSet<>());

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        InventoryHolder holder = topInventory.getHolder();

        // 1. Handle Anvil GUI Text Input Click
        if (holder instanceof AnvilInputHolder anvilHolder) {
            int slot = event.getRawSlot();

            // Lock slot 0 and slot 1 to prevent modification
            if (slot == 0 || slot == 1) {
                event.setCancelled(true);
                return;
            }

            // Cancel shifting/moving items from player inventory
            if (event.getClickedInventory() != topInventory) {
                if (event.isShiftClick()) {
                    event.setCancelled(true);
                }
                return;
            }

            event.setCancelled(true);

            // Clicked the result slot
            if (slot == 2) {
                Player player = (Player) event.getWhoClicked();
                ItemStack resultItem = topInventory.getItem(2);
                String resultText = "";
                if (resultItem != null && resultItem.hasItemMeta() && resultItem.getItemMeta().hasDisplayName()) {
                    resultText = resultItem.getItemMeta().getDisplayName();
                } else {
                    resultText = anvilHolder.getBuilder().getInitialText();
                }

                // Enforce max length
                int limit = anvilHolder.getBuilder().getMaxLength();
                if (resultText.length() > limit) {
                    resultText = resultText.substring(0, limit);
                }

                // Close inventory and execute callback
                anvilSubmitting.add(player.getUniqueId());
                player.closeInventory();
                anvilHolder.getBuilder().getOnSubmit().accept(player, resultText);
            }
            return;
        }

        // 2. Handle Chest GUI Click
        if (!(holder instanceof GuiHolder guiHolder)) {
            return;
        }

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

        Collection<GuiLayer> layers = gui.getLayers();
        List<GuiLayer> descendingLayers = new ArrayList<>(layers);
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
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof AnvilInputHolder anvilHolder) {
            Player player = (Player) event.getPlayer();
            if (!anvilSubmitting.remove(player.getUniqueId())) {
                anvilHolder.getBuilder().getOnClose().accept(player);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getView().getTopInventory().getHolder();
        if (holder instanceof GuiHolder || holder instanceof AnvilInputHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        SignInputBuilderImpl.SignSession session = SignInputBuilderImpl.ACTIVE_SESSIONS.remove(player);
        if (session != null) {
            event.setCancelled(true);
            session.restore();

            String text = event.getLine(1);
            if (text == null) {
                text = "";
            }

            int limit = session.getMaxLength();
            if (text.length() > limit) {
                text = text.substring(0, limit);
            }

            session.getOnSubmit().accept(player, text);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        SignInputBuilderImpl.SignSession session = SignInputBuilderImpl.ACTIVE_SESSIONS.remove(event.getPlayer());
        if (session != null) {
            session.restore();
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        SignInputBuilderImpl.SignSession session = SignInputBuilderImpl.ACTIVE_SESSIONS.remove(event.getPlayer());
        if (session != null) {
            session.restore();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        SignInputBuilderImpl.SignSession session = SignInputBuilderImpl.ACTIVE_SESSIONS.remove(event.getEntity());
        if (session != null) {
            session.restore();
        }
    }
}
