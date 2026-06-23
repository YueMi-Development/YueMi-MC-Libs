package org.yuemi.libs.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiClickContext;

public final class GuiClickContextImpl implements GuiClickContext {

    private final Player player;
    private final int slot;
    private final ClickType clickType;
    private final InventoryClickEvent event;
    private final Gui gui;

    public GuiClickContextImpl(
            @NotNull Player player,
            int slot,
            @NotNull ClickType clickType,
            @NotNull InventoryClickEvent event,
            @NotNull Gui gui) {
        this.player = player;
        this.slot = slot;
        this.clickType = clickType;
        this.event = event;
        this.gui = gui;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public @NotNull ClickType getClickType() {
        return clickType;
    }

    @Override
    public @NotNull InventoryClickEvent getEvent() {
        return event;
    }

    @Override
    public @NotNull Gui getGui() {
        return gui;
    }
}
