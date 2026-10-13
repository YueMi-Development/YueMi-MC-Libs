package org.yuemi.libs.plugin.gui;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.GuiApi;
import org.yuemi.libs.api.gui.GuiBuilder;
import org.yuemi.libs.api.gui.GuiItem;

import java.util.Collection;

public final class GuiApiImpl implements GuiApi {

    @Override
    public @NotNull GuiBuilder createBuilder() {
        return new GuiBuilderImpl();
    }

    @Override
    public @NotNull GuiItem.Builder createItemBuilder() {
        return new GuiItemImpl.BuilderImpl();
    }

    @Override
    public @NotNull org.yuemi.libs.api.gui.AnvilInputBuilder createAnvilInputBuilder() {
        return new AnvilInputBuilderImpl();
    }

    @Override
    public @NotNull org.yuemi.libs.api.gui.SignInputBuilder createSignInputBuilder() {
        return new SignInputBuilderImpl();
    }

    /**
     * Register slot positions that accept items being placed into them.
     * Used by the click handler to distinguish PLACE from SWAP when slots
     * contain placeholder items.
     */
    public static void registerInputSlots(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.addInputSlots(positions);
        }
    }

    /**
     * Remove slot positions from the input slot registry.
     */
    public static void unregisterInputSlots(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.removeInputSlots(positions);
        }
    }
}
