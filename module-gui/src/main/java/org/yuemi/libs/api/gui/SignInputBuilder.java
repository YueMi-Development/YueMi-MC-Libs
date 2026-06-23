package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiConsumer;

/**
 * Builder for creating Sign-based text input GUIs.
 */
public interface SignInputBuilder {

    /**
     * Sets the initial text in the edit line (Line 2).
     *
     * @param text the initial text
     * @return this builder
     */
    @NotNull
    SignInputBuilder initialText(@NotNull String text);

    /**
     * Sets the short description shown on Line 4 of the sign.
     *
     * @param description the description
     * @return this builder
     */
    @NotNull
    SignInputBuilder description(@NotNull String description);

    /**
     * Sets the maximum character limit for the input.
     *
     * @param limit the max character length
     * @return this builder
     */
    @NotNull
    SignInputBuilder maxLength(int limit);

    /**
     * Sets the callback executed when the player finishes editing the sign.
     * The callback receives the player and the typed text from Line 2.
     *
     * @param callback the submit callback
     * @return this builder
     */
    @NotNull
    SignInputBuilder onSubmit(@NotNull BiConsumer<Player, String> callback);

    /**
     * Opens the sign editor for the specified player.
     *
     * @param player the player
     */
    void open(@NotNull Player player);
}
