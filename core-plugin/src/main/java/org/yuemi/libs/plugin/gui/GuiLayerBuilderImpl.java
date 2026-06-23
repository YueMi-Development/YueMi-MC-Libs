package org.yuemi.libs.plugin.gui;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import org.yuemi.libs.api.gui.GuiLayerBuilder;

public final class GuiLayerBuilderImpl implements GuiLayerBuilder {

    private final GuiLayerImpl layer;

    public GuiLayerBuilderImpl(@NotNull String id, int priority) {
        this.layer = new GuiLayerImpl(id, priority);
    }

    @Override
    public @NotNull GuiLayerBuilder setItem(int slot, @NotNull GuiItem item) {
        layer.setItem(slot, item);
        return this;
    }

    @Override
    public @NotNull GuiLayerBuilder fill(@NotNull GuiItem item) {
        for (int i = 0; i < 54; i++) {
            layer.setItem(i, item);
        }
        return this;
    }

    @Override
    public @NotNull GuiLayer build() {
        return layer;
    }
}
