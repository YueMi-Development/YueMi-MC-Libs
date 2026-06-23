package org.yuemi.libs.plugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.ClosePolicy;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GuiImpl implements Gui {

    private final String title;
    private final int rows;
    private final Map<String, GuiLayer> layers = new ConcurrentHashMap<>();
    private ClosePolicy closePolicy = ClosePolicy.CLOSE;
    private java.util.function.Consumer<Player> onClose;

    public GuiImpl(@NotNull String title, int rows) {
        this.title = title;
        this.rows = Math.max(1, Math.min(6, rows));
    }

    @Override
    public @NotNull ClosePolicy getClosePolicy() {
        return closePolicy;
    }

    public void setClosePolicy(@NotNull ClosePolicy closePolicy) {
        this.closePolicy = closePolicy;
    }

    @Override
    public java.util.function.Consumer<Player> getOnClose() {
        return onClose;
    }

    public void setOnClose(java.util.function.Consumer<Player> onClose) {
        this.onClose = onClose;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public void addLayer(@NotNull GuiLayer layer) {
        layers.put(layer.getId(), layer);
    }

    @Override
    public void removeLayer(@NotNull String layerId) {
        layers.remove(layerId);
    }

    @Override
    public @NotNull Collection<GuiLayer> getLayers() {
        List<GuiLayer> sortedLayers = new ArrayList<>(layers.values());
        sortedLayers.sort(Comparator.comparingInt(GuiLayer::getPriority));
        return Collections.unmodifiableList(sortedLayers);
    }

    private @NotNull List<GuiLayer> getSortedLayersDescending() {
        List<GuiLayer> sortedLayers = new ArrayList<>(layers.values());
        sortedLayers.sort(Comparator.comparingInt(GuiLayer::getPriority).reversed());
        return sortedLayers;
    }

    @Override
    public void open(@NotNull Player player) {
        GuiHolder holder = new GuiHolder(this);
        Inventory inventory = Bukkit.createInventory(holder, rows * 9, title);
        holder.setInventory(inventory);

        populateInventory(player, inventory);
        player.openInventory(inventory);
    }

    @Override
    public void update(@NotNull Player player) {
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (topInventory.getHolder() instanceof GuiHolder holder) {
            if (holder.getGui() == this) {
                populateInventory(player, topInventory);
            }
        }
    }

    private void populateInventory(@NotNull Player player, @NotNull Inventory inventory) {
        int size = rows * 9;
        List<GuiLayer> descendingLayers = getSortedLayersDescending();

        for (int slot = 0; slot < size; slot++) {
            GuiItem matchedItem = null;
            for (GuiLayer layer : descendingLayers) {
                GuiItem item = layer.getItem(slot);
                if (item != null && item.shouldRender(player)) {
                    matchedItem = item;
                    break;
                }
            }

            if (matchedItem != null) {
                ItemStack resolved = matchedItem.getItemStack(player);
                inventory.setItem(slot, resolved);
            } else {
                inventory.setItem(slot, new ItemStack(Material.AIR));
            }
        }
    }
}
