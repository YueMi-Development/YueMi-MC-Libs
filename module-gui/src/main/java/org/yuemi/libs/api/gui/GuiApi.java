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

    /**
     * Creates a new fluent Anvil GUI builder for text input.
     *
     * @return a new Anvil GUI builder
     */
    @NotNull
    AnvilInputBuilder createAnvilInputBuilder();

    /**
     * Creates a new fluent Sign GUI builder for text input.
     *
     * @return a new Sign GUI builder
     */
    @NotNull
    SignInputBuilder createSignInputBuilder();
}
