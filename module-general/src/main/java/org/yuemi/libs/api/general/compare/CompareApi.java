package org.yuemi.libs.api.general.compare;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility API for comparing ItemStacks.
 */
public class CompareApi {

    private static final CompareApi INSTANCE = new CompareApi();

    public static CompareApi getInstance() {
        return INSTANCE;
    }

    private CompareApi() {}

    /**
     * Compares two ItemStacks for equality.
     *
     * @param item1 the first item
     * @param item2 the second item
     * @return the comparison result
     */
    @NotNull
    public ItemComparison compare(@Nullable ItemStack item1, @Nullable ItemStack item2) {
        if (item1 == null && item2 == null) {
            return ItemComparison.equal();
        }
        if (item1 == null || item2 == null) {
            return ItemComparison.different("One item is null");
        }

        boolean sameMaterial = item1.getType() == item2.getType();
        boolean sameAmount = item1.getAmount() == item2.getAmount();
        boolean sameDurability = item1.getDurability() == item2.getDurability();
        boolean sameMeta = item1.hasItemMeta() && item2.hasItemMeta() && item1.getItemMeta().equals(item2.getItemMeta());
        boolean sameEnchantments = item1.getEnchantments().equals(item2.getEnchantments());
        boolean sameCustomModelData = item1.hasItemMeta() && item2.hasItemMeta() 
            && item1.getItemMeta().hasCustomModelData() 
            && item2.getItemMeta().hasCustomModelData()
            && item1.getItemMeta().getCustomModelData() == item2.getItemMeta().getCustomModelData();

        boolean areEqual = sameMaterial && sameAmount && sameMeta && sameEnchantments && sameCustomModelData && sameDurability;
        boolean areSimilar = sameMaterial && sameAmount && sameEnchantments;

        if (areEqual) {
            return ItemComparison.equal();
        } else if (areSimilar) {
            return ItemComparison.similar(buildDifferenceReason(item1, item2, sameMaterial, sameAmount, sameMeta, sameEnchantments, sameCustomModelData, sameDurability));
        } else {
            return ItemComparison.different(buildDifferenceReason(item1, item2, sameMaterial, sameAmount, sameMeta, sameEnchantments, sameCustomModelData, sameDurability));
        }
    }

    @NotNull
    private String buildDifferenceReason(ItemStack item1, ItemStack item2, boolean sameMaterial, boolean sameAmount, boolean sameMeta, boolean sameEnchantments, boolean sameCustomModelData, boolean sameDurability) {
        StringBuilder reason = new StringBuilder();
        if (!sameMaterial) {
            reason.append("Different material (").append(item1.getType()).append(" vs ").append(item2.getType()).append("). ");
        }
        if (!sameAmount) {
            reason.append("Different amount (").append(item1.getAmount()).append(" vs ").append(item2.getAmount()).append("). ");
        }
        if (!sameDurability) {
            reason.append("Different durability (").append(item1.getDurability()).append(" vs ").append(item2.getDurability()).append("). ");
        }
        if (!sameMeta) {
            reason.append("Different metadata. ");
        }
        if (!sameEnchantments) {
            reason.append("Different enchantments. ");
        }
        if (!sameCustomModelData) {
            reason.append("Different CustomModelData. ");
        }
        return reason.toString().trim();
    }
}
