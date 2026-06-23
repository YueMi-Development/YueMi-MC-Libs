package org.yuemi.libs.plugin.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class AnvilInputHolder implements InventoryHolder {

    private final AnvilInputBuilderImpl builder;
    private Inventory inventory;

    public AnvilInputHolder(@NotNull AnvilInputBuilderImpl builder) {
        this.builder = builder;
    }

    public @NotNull AnvilInputBuilderImpl getBuilder() {
        return builder;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setInventory(@NotNull Inventory inventory) {
        this.inventory = inventory;
    }
}
