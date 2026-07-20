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
import org.bukkit.plugin.Plugin;
import org.yuemi.libs.api.gui.ClosePolicy;
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

    private final Plugin plugin;
    private final Set<UUID> anvilSubmitting = Collections.synchronizedSet(new HashSet<>());

    public GuiListener(Plugin plugin) {
        this.plugin = plugin;
    }

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
        Player player = (Player) event.getPlayer();

        // 1. Handle Anvil GUI Close
        if (holder instanceof AnvilInputHolder anvilHolder) {
            if (!anvilSubmitting.remove(player.getUniqueId())) {
                ClosePolicy policy = anvilHolder.getBuilder().getClosePolicy();
                if (policy == ClosePolicy.REOPEN) {
                    org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> anvilHolder.getBuilder().open(player));
                } else if (policy == ClosePolicy.CLOSE) {
                    anvilHolder.getBuilder().getOnClose().accept(player);
                }
            }
            return;
        }

        // 2. Handle Chest GUI Close
        if (holder instanceof GuiHolder guiHolder) {
            Gui gui = guiHolder.getGui();
            ClosePolicy policy = gui.getClosePolicy();
            if (policy == ClosePolicy.REOPEN) {
                org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> gui.open(player));
            } else if (policy == ClosePolicy.CLOSE && gui.getOnClose() != null) {
                gui.getOnClose().accept(player);
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
    public void onPrepareAnvil(org.bukkit.event.inventory.PrepareAnvilEvent event) {
        if (event.getInventory().getHolder() instanceof AnvilInputHolder) {
            org.bukkit.inventory.AnvilInventory anvil = event.getInventory();
            anvil.setRepairCost(0);
            
            ItemStack leftItem = anvil.getItem(0);
            if (leftItem != null) {
                String renameText = anvil.getRenameText();
                ItemStack result = leftItem.clone();
                org.bukkit.inventory.meta.ItemMeta meta = result.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(renameText != null ? renameText : "");
                    result.setItemMeta(meta);
                }
                event.setResult(result);
            }
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

            // Enforce reopen close policy if closed/submitted empty or unchanged text
            if (session.getClosePolicy() == ClosePolicy.REOPEN && (text.isEmpty() || text.equals(session.getInitialText()))) {
                org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> session.getBuilder().open(player));
                return;
            }

            // Enforce nothing close policy if closed/submitted empty or unchanged text
            if (session.getClosePolicy() == ClosePolicy.NOTHING && (text.isEmpty() || text.equals(session.getInitialText()))) {
                return;
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
