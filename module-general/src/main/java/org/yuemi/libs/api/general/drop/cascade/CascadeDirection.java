package org.yuemi.libs.api.general.drop.cascade;

/**
 * Controls the order in which rarity tiers are evaluated during a drop roll.
 */
public enum CascadeDirection {

    /**
     * Checks tiers from lowest rarity (index 0) to highest.
     * The first tier to hit wins — higher rariy tiers are only reached
     * if all lower tiers missed.
     */
    LOW_TO_HIGH,

    /**
     * Checks tiers from highest rarity (last added) down to lowest (index 0).
     * The first tier to hit wins — lower rarity tiers are only reached
     * if all higher tiers missed.
     */
    HIGH_TO_LOW
}
