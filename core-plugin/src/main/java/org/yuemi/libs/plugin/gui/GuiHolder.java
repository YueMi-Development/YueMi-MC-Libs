package org.yuemi.libs.plugin.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.Gui;

/**
 * Custom InventoryHolder to associate Bukkit inventories with our Gui instances.
 */
public final class GuiHolder implements InventoryHolder {

    private final Gui gui;
    private Inventory inventory;

    public GuiHolder(@NotNull Gui gui) {
        this.gui = gui;
    }

    @NotNull
    public Gui getGui() {
        return gui;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }
}
