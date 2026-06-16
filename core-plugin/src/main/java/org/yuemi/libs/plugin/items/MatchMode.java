package org.yuemi.libs.plugin.items;

/**
 * Strategy for matching items in player inventories.
 */
public enum MatchMode {
    /**
     * Matches only by Material ID type (e.g. minecraft:stone).
     */
    ID,

    /**
     * Matches by Material ID type and Custom Model Data.
     */
    CUSTOM_MODEL_DATA,

    /**
     * Matches strictly (requires matching NBT/item similarity/components).
     */
    NBT
}
