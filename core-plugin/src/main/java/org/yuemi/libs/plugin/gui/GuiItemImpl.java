package org.yuemi.libs.plugin.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.GuiClickContext;
import org.yuemi.libs.api.gui.GuiItem;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class GuiItemImpl implements GuiItem {

    private final Function<Player, ItemStack> itemProvider;
    private final Predicate<Player> condition;
    private final BiConsumer<Player, GuiClickContext> handler;

    private GuiItemImpl(
            @NotNull Function<Player, ItemStack> itemProvider,
            @NotNull Predicate<Player> condition,
            @NotNull BiConsumer<Player, GuiClickContext> handler) {
        this.itemProvider = itemProvider;
        this.condition = condition;
        this.handler = handler;
    }

    @Override
    public @NotNull ItemStack getItemStack(@NotNull Player player) {
        return itemProvider.apply(player);
    }

    @Override
    public boolean shouldRender(@NotNull Player player) {
        return condition.test(player);
    }

    @Override
    public void onClick(@NotNull GuiClickContext context) {
        handler.accept(context.getPlayer(), context);
    }

    public static final class BuilderImpl implements GuiItem.Builder {

        private Function<Player, ItemStack> itemProvider;
        private Predicate<Player> condition = player -> true;
        private BiConsumer<Player, GuiClickContext> handler = (player, context) -> {};

        @Override
        public @NotNull GuiItem.Builder item(@NotNull ItemStack itemStack) {
            this.itemProvider = player -> itemStack.clone();
            return this;
        }

        @Override
        public @NotNull GuiItem.Builder item(@NotNull Function<Player, ItemStack> itemProvider) {
            this.itemProvider = itemProvider;
            return this;
        }

        @Override
        public @NotNull GuiItem.Builder item(@NotNull String key) {
            return item(key, 1);
        }

        @Override
        public @NotNull GuiItem.Builder item(@NotNull String key, int amount) {
            this.itemProvider = player -> {
                var api = org.yuemi.libs.api.items.ItemsApiProvider.getApi();
                if (api == null) {
                    throw new IllegalStateException("ItemsApi provider is not registered!");
                }
                ItemStack item = api.getItem(key, amount);
                return item != null ? item : new ItemStack(org.bukkit.Material.AIR);
            };
            return this;
        }

        @Override
        public @NotNull GuiItem.Builder itemKey(@NotNull Function<Player, String> keyProvider) {
            return itemKey(keyProvider, 1);
        }

        @Override
        public @NotNull GuiItem.Builder itemKey(@NotNull Function<Player, String> keyProvider, int amount) {
            this.itemProvider = player -> {
                var api = org.yuemi.libs.api.items.ItemsApiProvider.getApi();
                if (api == null) {
                    throw new IllegalStateException("ItemsApi provider is not registered!");
                }
                String key = keyProvider.apply(player);
                if (key == null) {
                    return new ItemStack(org.bukkit.Material.AIR);
                }
                ItemStack item = api.getItem(key, amount);
                return item != null ? item : new ItemStack(org.bukkit.Material.AIR);
            };
            return this;
        }

        @Override
        public @NotNull GuiItem.Builder condition(@NotNull Predicate<Player> condition) {
            this.condition = condition;
            return this;
        }

        @Override
        public @NotNull GuiItem.Builder onClick(@NotNull BiConsumer<Player, GuiClickContext> handler) {
            this.handler = handler;
            return this;
        }

        @Override
        public @NotNull GuiItem build() {
            if (itemProvider == null) {
                throw new IllegalStateException("ItemStack or ItemProvider must be configured for GuiItem");
            }
            return new GuiItemImpl(itemProvider, condition, handler);
        }
    }
}
