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

    static Builder builder() {
        var api = org.yuemi.libs.api.YueMiLibsProvider.getApi();
        if (api == null) {
            throw new IllegalStateException("YueMiLibsApi is not registered!");
        }
        return api.getGui().createItemBuilder();
    }

    /**
     * Builder interface for constructing GuiItems.
     */
    interface Builder {

        @NotNull
        Builder item(@NotNull ItemStack itemStack);

        @NotNull
        Builder item(@NotNull Function<Player, ItemStack> itemProvider);

        @NotNull
        Builder condition(@NotNull Predicate<Player> condition);

        @NotNull
        Builder onClick(@NotNull BiConsumer<Player, GuiClickContext> handler);

        @NotNull
        GuiItem build();
    }
}
