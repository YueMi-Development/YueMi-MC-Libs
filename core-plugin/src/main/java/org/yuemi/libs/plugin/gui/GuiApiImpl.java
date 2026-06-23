package org.yuemi.libs.plugin.gui;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.GuiApi;
import org.yuemi.libs.api.gui.GuiBuilder;
import org.yuemi.libs.api.gui.GuiItem;

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
}
