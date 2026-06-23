package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Builder for creating Anvil-based text input GUIs.
 */
public interface AnvilInputBuilder {

    /**
     * Sets the title of the anvil GUI.
     *
     * @param title the title
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder title(@NotNull String title);

    /**
     * Sets the initial text in the rename slot.
     *
     * @param text the initial text
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder initialText(@NotNull String text);

    /**
     * Sets the left input item stack.
     *
     * @param item the item stack
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder leftItem(@NotNull ItemStack item);

    /**
     * Sets the maximum character limit for the input.
     *
     * @param limit the max character length
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder maxLength(int limit);

    /**
     * Sets the callback executed when the player submits the input (clicks the output slot).
     * The callback receives the player and the typed text.
     *
     * @param callback the submit callback
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder onSubmit(@NotNull BiConsumer<Player, String> callback);

    /**
     * Sets the callback executed when the GUI is closed without submitting.
     *
     * @param callback the close callback
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder onClose(@NotNull Consumer<Player> callback);

    /**
     * Sets the close policy for this Anvil GUI.
     *
     * @param policy the close policy
     * @return this builder
     */
    @NotNull
    AnvilInputBuilder closePolicy(@NotNull ClosePolicy policy);

    /**
     * Opens the anvil GUI for the specified player.
     *
     * @param player the player
     */
    void open(@NotNull Player player);
}
