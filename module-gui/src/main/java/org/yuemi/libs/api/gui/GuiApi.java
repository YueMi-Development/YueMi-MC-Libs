package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;

/**
 * Entry point for creating GUI instances.
 */
public interface GuiApi {

    /**
     * Creates a new fluent GUI builder.
     *
     * @return a new GUI builder
     */
    @NotNull
    GuiBuilder createBuilder();

    /**
     * Creates a new fluent GUI Item builder.
     *
     * @return a new GUI Item builder
     */
    @NotNull
    GuiItem.Builder createItemBuilder();
}
