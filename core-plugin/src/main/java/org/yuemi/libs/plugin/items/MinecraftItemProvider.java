package org.yuemi.libs.plugin.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.items.ItemProvider;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MinecraftItemProvider implements ItemProvider {

    private MatchMode matchMode;

    public MinecraftItemProvider(@NotNull MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    public @NotNull MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(@NotNull MatchMode matchMode) {
        this.matchMode = matchMode;
    }

    @Override
    public @NotNull String getName() {
        return "Minecraft";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private static class ParsedItemInfo {
        final String baseMaterialName;
        final @Nullable Integer customModelData;

        ParsedItemInfo(String baseMaterialName, @Nullable Integer customModelData) {
            this.baseMaterialName = baseMaterialName;
            this.customModelData = customModelData;
        }
    }

    private @NotNull ParsedItemInfo parseItemInfo(@NotNull String id) {
        int braceIdx = id.indexOf('{');
        if (braceIdx == -1) {
            return new ParsedItemInfo(id, null);
        }
        String base = id.substring(0, braceIdx);
        String nbt = id.substring(braceIdx + 1, id.length() - 1);
        Pattern pattern = Pattern.compile("CustomModelData:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(nbt);
        Integer cmd = null;
        if (matcher.find()) {
            try {
                cmd = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return new ParsedItemInfo(base, cmd);
    }

    private boolean matches(@NotNull ItemStack playerItem, @NotNull ItemStack targetTemplate, @Nullable Integer targetCmd) {
        if (playerItem.getType() != targetTemplate.getType()) {
            return false;
        }
        switch (matchMode) {
            case ID:
                return true;
            case CUSTOM_MODEL_DATA:
                ItemMeta playerMeta = playerItem.hasItemMeta() ? playerItem.getItemMeta() : null;
                Integer playerCmd = (playerMeta != null && playerMeta.hasCustomModelData()) ? playerMeta.getCustomModelData() : null;
                if (targetCmd == null) {
                    return playerCmd == null;
                } else {
                    return targetCmd.equals(playerCmd);
                }
            case NBT:
                return playerItem.isSimilar(targetTemplate);
            default:
                return false;
        }
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull String id, int amount) {
        ParsedItemInfo info = parseItemInfo(id);
        Material material = Material.matchMaterial(info.baseMaterialName);
        if (material == null || material.isAir()) {
            return null;
        }
        ItemStack item = new ItemStack(material, amount);
        if (info.customModelData != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(info.customModelData);
                item.setItemMeta(meta);
            }
        }
        return item;
    }

    @Override
    public boolean giveItem(@NotNull Player player, @NotNull String id, int amount) {
        ItemStack item = getItem(id, amount);
        if (item == null) {
            return false;
        }
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        if (!leftovers.isEmpty()) {
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), leftover);
            }
        }
        return true;
    }

    @Override
    public boolean takeItem(@NotNull Player player, @NotNull String id, int amount) {
        if (amount <= 0) {
            return true;
        }
        ItemStack targetTemplate = getItem(id, 1);
        if (targetTemplate == null) {
            return false;
        }
        ParsedItemInfo info = parseItemInfo(id);

        int totalCount = getItemCount(player, id);
        if (totalCount < amount) {
            return false;
        }

        int remainingToTake = amount;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && matches(item, targetTemplate, info.customModelData)) {
                int count = item.getAmount();
                if (count <= remainingToTake) {
                    remainingToTake -= count;
                    player.getInventory().setItem(i, null);
                } else {
                    item.setAmount(count - remainingToTake);
                    remainingToTake = 0;
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public int getItemCount(@NotNull Player player, @NotNull String id) {
        ItemStack targetTemplate = getItem(id, 1);
        if (targetTemplate == null) {
            return 0;
        }
        ParsedItemInfo info = parseItemInfo(id);

        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && matches(item, targetTemplate, info.customModelData)) {
                count += item.getAmount();
            }
        }
        return count;
    }
}
