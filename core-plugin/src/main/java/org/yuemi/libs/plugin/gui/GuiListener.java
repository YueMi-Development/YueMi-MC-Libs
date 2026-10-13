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
import org.yuemi.libs.api.gui.GuiClickContext;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import org.yuemi.libs.api.gui.SlotBehavior;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GuiListener implements Listener {

    private final Plugin plugin;
    private final Set<UUID> anvilSubmitting = Collections.synchronizedSet(new HashSet<>());
    // Set of slot positions that accept items being placed into them
    // (across all registered GUIs). Used to distinguish PLACE from SWAP
    // when the slot has a placeholder item.
    private final Set<Integer> inputSlotPositions = ConcurrentHashMap.newKeySet();

    private static volatile GuiListener instance;

    public static GuiListener getInstance() {
        return instance;
    }

    public GuiListener(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    /**
     * Add input slot positions. Called by plugins that want their slots
     * to accept items being placed into them.
     */
    public void addInputSlots(java.util.Collection<Integer> positions) {
        inputSlotPositions.addAll(positions);
    }

    /**
     * Remove input slot positions.
     */
    public void removeInputSlots(java.util.Collection<Integer> positions) {
        inputSlotPositions.removeAll(positions);
    }

    public Set<Integer> getInputSlotPositions() {
        return Collections.unmodifiableSet(inputSlotPositions);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        InventoryHolder holder = topInventory.getHolder();

        // 1. Handle Anvil GUI Text Input Click
        Player player = (Player) event.getWhoClicked();
        AnvilInputBuilderImpl anvilHolder = AnvilInputBuilderImpl.ACTIVE_SESSIONS.get(player.getUniqueId());
        if (anvilHolder == null && holder instanceof AnvilInputHolder) {
            anvilHolder = (AnvilInputBuilderImpl) ((AnvilInputHolder) holder).getBuilder();
        }

        // 1. Handle Anvil GUI Text Input Click
        if (anvilHolder != null) {
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
                ItemStack resultItem = topInventory.getItem(2);
                String resultText = "";
                if (resultItem != null && resultItem.hasItemMeta() && resultItem.getItemMeta().hasDisplayName()) {
                    resultText = resultItem.getItemMeta().getDisplayName();
                } else {
                    resultText = anvilHolder.getInitialText();
                }
                
                resultText = org.bukkit.ChatColor.stripColor(resultText);

                // Enforce max length
                int limit = anvilHolder.getMaxLength();
                if (resultText.length() > limit) {
                    resultText = resultText.substring(0, limit);
                }

                // Close inventory and execute callback
                anvilSubmitting.add(player.getUniqueId());
                topInventory.clear();
                player.setItemOnCursor(null);
                
                final String finalResultText = resultText;
                final AnvilInputBuilderImpl finalAnvilHolder = anvilHolder;
                
                boolean isVirtual = holder instanceof AnvilInputHolder;
                if (isVirtual) {
                    player.closeInventory();
                    finalAnvilHolder.getOnSubmit().accept(player, finalResultText);
                } else {
                    org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> {
                        player.closeInventory();
                        finalAnvilHolder.getOnSubmit().accept(player, finalResultText);
                    });
                }
            }
            return;
        }

        // 2. Handle Chest GUI Click
        if (!(holder instanceof GuiHolder guiHolder)) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();

        // If clicked in the player's bottom inventory, allow normal inventory behavior
        // unless it's a shift-click (item would be moved to top inventory)
        if (clickedInventory != null && !(clickedInventory.getHolder() instanceof GuiHolder)) {
            if (event.isShiftClick()) {
                handleShiftClickIntoGui(event, guiHolder);
            }
            return;  // Don't cancel - let inventory handle it
        }

        if (clickedInventory == null) {
            return;
        }

        int slot = event.getSlot();
        if (slot < 0 || slot >= clickedInventory.getSize()) {
            return;
        }

        Gui gui = guiHolder.getGui();

        GuiItem clickedItem = findGuiItem(gui, slot, player);

        if (clickedItem == null) {
            // No GUI item here - allow normal chest interaction
            return;
        }

        // Check per-slot behavior
        SlotBehavior behavior = clickedItem.behavior();
        org.yuemi.libs.api.gui.GuiClickContext.Action action = determineAction(event, event.getSlot(), gui);

        if (!isActionAllowed(behavior, action, event)) {
            event.setCancelled(true);
            return;
        }

        // For PICKUP and SWAP actions that are allowed, let the inventory handle
        // the item movement but still notify the GUI item (so it can react).
        boolean allowNativeMovement = switch (action) {
            case PICKUP, SWAP, DROP_CURSOR -> behavior.allowPickup();
            default -> false;
        };

        if (!allowNativeMovement) {
            // Cancel and dispatch - GUI controls the item
            event.setCancelled(true);
        }

        GuiClickContextImpl context = new GuiClickContextImpl(
                player,
                slot,
                event.getClick(),
                event,
                gui,
                action,
                determineInteractedItem(event)
        );
        clickedItem.onClick(context);
    }

    /**
     * Find the topmost GuiItem for a given slot.
     */
    private GuiItem findGuiItem(Gui gui, int slot, Player player) {
        Collection<GuiLayer> layers = gui.getLayers();
        List<GuiLayer> descendingLayers = new ArrayList<>(layers);
        descendingLayers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        for (GuiLayer layer : descendingLayers) {
            GuiItem item = layer.getItem(slot);
            if (item != null && item.shouldRender(player)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Check if the given action is allowed by the slot's behavior config.
     */
    private boolean isActionAllowed(SlotBehavior behavior,
                                    org.yuemi.libs.api.gui.GuiClickContext.Action action,
                                    InventoryClickEvent event) {
        if (event.isShiftClick()) {
            return behavior.allowShift();
        }
        return switch (action) {
            case PICKUP, SWAP, DROP_CURSOR -> behavior.allowPickup();
            case PLACE, SHIFT_CLICK -> behavior.allowPlace();
            case DRAG_PLACE -> behavior.allowDrag();
            case LEFT_CLICK, RIGHT_CLICK -> true; // Always allow clicks
        };
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        Player player = (Player) event.getPlayer();

        AnvilInputBuilderImpl anvilHolder = AnvilInputBuilderImpl.ACTIVE_SESSIONS.remove(player.getUniqueId());
        if (anvilHolder == null && holder instanceof AnvilInputHolder) {
            anvilHolder = (AnvilInputBuilderImpl) ((AnvilInputHolder) holder).getBuilder();
        }

        // 1. Handle Anvil GUI Close
        if (anvilHolder != null) {
            final AnvilInputBuilderImpl finalHolder = anvilHolder;
            if (!anvilSubmitting.remove(player.getUniqueId())) {
                ClosePolicy policy = finalHolder.getClosePolicy();
                if (policy == ClosePolicy.REOPEN) {
                    org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> finalHolder.open(player));
                } else if (policy == ClosePolicy.CLOSE) {
                    finalHolder.getOnClose().accept(player);
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
        Player player = (Player) event.getWhoClicked();
        if (AnvilInputBuilderImpl.ACTIVE_SESSIONS.containsKey(player.getUniqueId()) || holder instanceof AnvilInputHolder) {
            event.setCancelled(true);
            return;
        }

        // Not our GUI - ignore
        if (!(holder instanceof GuiHolder guiHolder)) {
            return;
        }

        int topSize = event.getView().getTopInventory().getSize();

        // Check if any of the affected slots are in the top (GUI) inventory
        boolean affectsTopInventory = false;
        for (int slot : event.getRawSlots()) {
            if (slot < topSize) {
                affectsTopInventory = true;
                break;
            }
        }

        if (!affectsTopInventory) {
            // Drag is entirely in bottom inventory - allow
            return;
        }

        // Check per-slot behaviors to see if any slot disallows drag
        Gui gui = guiHolder.getGui();
        for (int slot : event.getRawSlots()) {
            if (slot >= topSize) continue;
            GuiItem item = findGuiItem(gui, slot, player);
            if (item != null && !item.behavior().allowDrag()) {
                // Some slot disallows drag - cancel entire drag
                event.setCancelled(true);
                return;
            }
        }

        // Cancel the drag and notify GUI for each affected top-inventory slot
        event.setCancelled(true);

        for (int slot : event.getRawSlots()) {
            if (slot >= topSize) continue;

            GuiItem clickedItem = findGuiItem(gui, slot, player);

            if (clickedItem != null) {
                // Build a synthetic click event context for the drag.
                org.bukkit.event.inventory.InventoryClickEvent syntheticClick = new org.bukkit.event.inventory.InventoryClickEvent(
                        event.getView(),
                        org.bukkit.event.inventory.InventoryType.SlotType.CONTAINER,
                        slot,
                        org.bukkit.event.inventory.ClickType.LEFT,
                        org.bukkit.event.inventory.InventoryAction.PLACE_ALL
                );
                ItemStack cursor = event.getOldCursor();
                GuiClickContextImpl context = new GuiClickContextImpl(
                        player,
                        slot,
                        org.bukkit.event.inventory.ClickType.LEFT,
                        syntheticClick,
                        gui,
                        GuiClickContextImpl.Action.DRAG_PLACE,
                        cursor != null ? cursor.clone() : null
                );
                clickedItem.onClick(context);
            }
        }
    }

    private void handleShiftClickIntoGui(InventoryClickEvent event, GuiHolder guiHolder) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) return;

        // Cancel the shift-click to take control
        event.setCancelled(true);

        Gui gui = guiHolder.getGui();
        int topSize = event.getView().getTopInventory().getSize();
        Collection<GuiLayer> layers = gui.getLayers();
        List<GuiLayer> descendingLayers = new ArrayList<>(layers);
        descendingLayers.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));

        // Find first slot in top inventory that has a GuiItem
        for (int slot = 0; slot < topSize; slot++) {
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
                        org.bukkit.event.inventory.ClickType.SHIFT_LEFT,
                        event,
                        gui,
                        GuiClickContextImpl.Action.SHIFT_CLICK,
                        clicked.clone()
                );
                clickedItem.onClick(context);
                break;  // Only one slot
            }
        }
    }

    private GuiClickContextImpl.Action determineAction(InventoryClickEvent event) {
        return determineAction(event, event.getSlot(), null);
    }

    private GuiClickContextImpl.Action determineAction(InventoryClickEvent event, int slot) {
        return determineAction(event, slot, null);
    }

    private GuiClickContextImpl.Action determineAction(InventoryClickEvent event, int slot, Gui gui) {
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();

        if (event.isShiftClick()) {
            return GuiClickContextImpl.Action.SHIFT_CLICK;
        }
        if (event.getAction().name().startsWith("HOTBAR_SWAP") || event.getAction() == org.bukkit.event.inventory.InventoryAction.HOTBAR_SWAP) {
            return GuiClickContextImpl.Action.SWAP;
        }
        if (cursor != null && !cursor.getType().isAir()) {
            // Player has a cursor item. Check if the current item in the slot is
            // a placeholder (not a real player item) by looking at the slot's GuiItem behavior.
            // The listener can be configured with a list of "input slot" positions
            // where items can be placed.
            if (current == null || current.getType().isAir()) {
                return GuiClickContextImpl.Action.PLACE;
            }
            // Per-GUI input slots, then global input slots registered via GuiApi.
            if (gui != null && gui.getInputSlots().contains(slot)) {
                return GuiClickContextImpl.Action.PLACE;
            }
            if (inputSlotPositions.contains(slot)) {
                return GuiClickContextImpl.Action.PLACE;
            }
            // Otherwise it's a swap with existing item
            return GuiClickContextImpl.Action.SWAP;
        }
        if ((cursor == null || cursor.getType().isAir()) && current != null && !current.getType().isAir()) {
            return GuiClickContextImpl.Action.PICKUP;
        }
        return GuiClickContextImpl.Action.LEFT_CLICK;
    }

    private ItemStack determineInteractedItem(InventoryClickEvent event) {
        return determineInteractedItem(event, event.getSlot());
    }

    private ItemStack determineInteractedItem(InventoryClickEvent event, int slot) {
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();
        if (cursor != null && !cursor.getType().isAir()) {
            return cursor.clone();
        }
        if (current != null && !current.getType().isAir()) {
            return current.clone();
        }
        return null;
    }

    @EventHandler
    public void onPrepareAnvil(org.bukkit.event.inventory.PrepareAnvilEvent event) {
        boolean isCustomAnvil = false;
        if (event.getInventory().getHolder() instanceof AnvilInputHolder) {
            isCustomAnvil = true;
        } else {
            for (org.bukkit.entity.HumanEntity viewer : event.getViewers()) {
                if (AnvilInputBuilderImpl.ACTIVE_SESSIONS.containsKey(viewer.getUniqueId())) {
                    isCustomAnvil = true;
                    break;
                }
            }
        }

        if (isCustomAnvil) {
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
