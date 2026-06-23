package org.yuemi.libs.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents an interactive item slot in a GUI layout layer.
 */
public interface GuiItem {

    /**
     * Resolves the ItemStack representation for the specified player.
     *
     * @param player the viewer player
     * @return the rendered item stack
     */
    @NotNull
    ItemStack getItemStack(@NotNull Player player);

    /**
     * Checks if this item should be rendered for the specified player.
     *
     * @param player the viewer player
     * @return true if it should render, false otherwise
     */
    boolean shouldRender(@NotNull Player player);

    /**
     * Executes the interaction callback when this item is clicked.
     *
     * @param context the click context details
     */
    void onClick(@NotNull GuiClickContext context);

    /**
     * Creates a new builder for constructing a {@link GuiItem}.
     *
     * @return a new GuiItem builder
     * @throws IllegalStateException if the GUI API provider is not registered
     */
    static Builder builder() {
        var api = GuiProvider.getApi();
        if (api == null) {
            throw new IllegalStateException("GuiApi provider is not registered!");
        }
        return api.createItemBuilder();
    }

    /**
     * Builder interface for constructing GuiItems.
     */
    interface Builder {

        /**
         * Sets a static item stack for the GUI item.
         *
         * @param itemStack the item stack to display
         * @return this builder instance for chaining
         */
        @NotNull
        Builder item(@NotNull ItemStack itemStack);

        /**
         * Sets a dynamic item provider function to resolve the item stack for a player.
         *
         * @param itemProvider the function to compute the item stack based on the player
         * @return this builder instance for chaining
         */
        @NotNull
        Builder item(@NotNull Function<Player, ItemStack> itemProvider);

        /**
         * Sets a condition that determines whether this item should render for a player.
         *
         * @param condition the condition predicate
         * @return this builder instance for chaining
         */
        @NotNull
        Builder condition(@NotNull Predicate<Player> condition);

        /**
         * Sets the handler to execute when the GUI item is clicked.
         *
         * @param handler the click interaction handler
         * @return this builder instance for chaining
         */
        @NotNull
        Builder onClick(@NotNull BiConsumer<Player, GuiClickContext> handler);

        /**
         * Builds the GUI item based on the configured properties.
         *
         * @return the constructed GuiItem
         */
        @NotNull
        GuiItem build();
    }
}
