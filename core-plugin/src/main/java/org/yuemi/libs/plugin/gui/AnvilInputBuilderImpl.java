package org.yuemi.libs.plugin.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.AnvilInputBuilder;
import org.yuemi.libs.api.gui.ClosePolicy;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class AnvilInputBuilderImpl implements AnvilInputBuilder {

    private String title = "Input";
    private String initialText = "";
    private ItemStack leftItem;
    private int maxLength = Integer.MAX_VALUE;
    private BiConsumer<Player, String> onSubmit = (player, text) -> {};
    private Consumer<Player> onClose = player -> {};
    private ClosePolicy closePolicy = ClosePolicy.CLOSE;

    @Override
    public @NotNull AnvilInputBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder initialText(@NotNull String text) {
        this.initialText = text;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder leftItem(@NotNull ItemStack item) {
        this.leftItem = item;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder maxLength(int limit) {
        this.maxLength = limit;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder onSubmit(@NotNull BiConsumer<Player, String> callback) {
        this.onSubmit = callback;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder onClose(@NotNull Consumer<Player> callback) {
        this.onClose = callback;
        return this;
    }

    @Override
    public @NotNull AnvilInputBuilder closePolicy(@NotNull ClosePolicy policy) {
        this.closePolicy = policy;
        return this;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public @NotNull String getInitialText() {
        return initialText;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public @NotNull BiConsumer<Player, String> getOnSubmit() {
        return onSubmit;
    }

    public @NotNull Consumer<Player> getOnClose() {
        return onClose;
    }

    public @NotNull ClosePolicy getClosePolicy() {
        return closePolicy;
    }

    @Override
    public void open(@NotNull Player player) {
        AnvilInputHolder holder = new AnvilInputHolder(this);
        Inventory inventory = Bukkit.createInventory(holder, org.bukkit.event.inventory.InventoryType.ANVIL, title);
        holder.setInventory(inventory);

        ItemStack item = leftItem != null ? leftItem.clone() : new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(initialText);
            item.setItemMeta(meta);
        }
        inventory.setItem(0, item);

        player.openInventory(inventory);
    }
}
