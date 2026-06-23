package org.yuemi.libs.api.economy;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a specific economy backend (e.g. Vault, PlayerPoints, custom, etc.).
 */
public interface EconomyProvider {

    /**
     * Gets the unique name of this provider.
     *
     * @return the provider name
     */
    @NotNull
    String getName();

    /**
     * Checks if this economy provider is available and ready for transactions.
     *
     * @return true if available, false otherwise
     */
    boolean isAvailable();

    /**
     * Gets the balance of a player.
     *
     * @param player the player
     * @return the balance, or 0.0 if not available or player has no account
     */
    double getBalance(@NotNull OfflinePlayer player);

    /**
     * Withdraws an amount from a player's balance.
     *
     * @param player the player
     * @param amount the amount to withdraw
     * @return true if successful, false otherwise
     */
    boolean withdraw(@NotNull OfflinePlayer player, double amount);

    /**
     * Deposits an amount into a player's balance.
     *
     * @param player the player
     * @param amount the amount to deposit
     * @return true if successful, false otherwise
     */
    boolean deposit(@NotNull OfflinePlayer player, double amount);

    /**
     * Sets a player's balance to a specific amount.
     *
     * @param player the player
     * @param amount the new balance amount
     * @return true if successful, false otherwise
     */
    boolean setBalance(@NotNull OfflinePlayer player, double amount);
}
