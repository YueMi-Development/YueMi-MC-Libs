package org.yuemi.libs.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiClickContext;

public final class GuiClickContextImpl implements GuiClickContext {

    private final Player player;
    private final int slot;
    private final ClickType clickType;
    private final InventoryClickEvent event;
    private final Gui gui;
    private final Action action;
    private final ItemStack interactedItem;

    public GuiClickContextImpl(
            @NotNull Player player,
            int slot,
            @NotNull ClickType clickType,
            @NotNull InventoryClickEvent event,
            @NotNull Gui gui) {
        this(player, slot, clickType, event, gui, GuiClickContext.Action.LEFT_CLICK, null);
    }

    public GuiClickContextImpl(
            @NotNull Player player,
            int slot,
            @NotNull ClickType clickType,
            @NotNull InventoryClickEvent event,
            @NotNull Gui gui,
            @NotNull GuiClickContext.Action action,
            @Nullable ItemStack interactedItem) {
        this.player = player;
        this.slot = slot;
        this.clickType = clickType;
        this.event = event;
        this.gui = gui;
        this.action = action;
        this.interactedItem = interactedItem;
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

    @Override
    public @NotNull Action getAction() {
        return action;
    }

    @Override
    public @Nullable ItemStack getInteractedItem() {
        return interactedItem;
    }
}
