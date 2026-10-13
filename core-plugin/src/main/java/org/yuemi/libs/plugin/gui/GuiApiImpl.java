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

    @Override
    public void registerInputSlots(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.addInputSlots(positions);
        }
    }

    @Override
    public void unregisterInputSlots(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.removeInputSlots(positions);
        }
    }

    /**
     * Static accessor kept for backwards compatibility with plugins that call this
     * method via reflection. Prefer {@link #registerInputSlots(Collection)} for
     * compile-time access.
     */
    public static void registerInputSlotsStatic(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.addInputSlots(positions);
        }
    }

    /**
     * Static accessor kept for backwards compatibility.
     */
    public static void unregisterInputSlotsStatic(@NotNull Collection<Integer> positions) {
        GuiListener listener = GuiListener.getInstance();
        if (listener != null) {
            listener.removeInputSlots(positions);
        }
    }
}
