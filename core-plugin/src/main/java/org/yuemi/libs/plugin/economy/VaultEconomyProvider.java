package org.yuemi.libs.plugin.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.economy.EconomyProvider;

public final class VaultEconomyProvider implements EconomyProvider {

    private @Nullable Economy getVaultEconomy() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        return rsp != null ? rsp.getProvider() : null;
    }

    @Override
    public @NotNull String getName() {
        return "Vault";
    }

    @Override
    public boolean isAvailable() {
        return getVaultEconomy() != null;
    }

    @Override
    public double getBalance(@NotNull OfflinePlayer player) {
        Economy econ = getVaultEconomy();
        if (econ == null) {
            return 0.0;
        }
        return econ.getBalance(player);
    }

    @Override
    public boolean withdraw(@NotNull OfflinePlayer player, double amount) {
        Economy econ = getVaultEconomy();
        if (econ == null || amount < 0) {
            return false;
        }
        return econ.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean deposit(@NotNull OfflinePlayer player, double amount) {
        Economy econ = getVaultEconomy();
        if (econ == null || amount < 0) {
            return false;
        }
        return econ.depositPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean setBalance(@NotNull OfflinePlayer player, double amount) {
        Economy econ = getVaultEconomy();
        if (econ == null || amount < 0) {
            return false;
        }
        double current = econ.getBalance(player);
        double diff = amount - current;
        if (diff > 0) {
            return econ.depositPlayer(player, diff).transactionSuccess();
        } else if (diff < 0) {
            return econ.withdrawPlayer(player, -diff).transactionSuccess();
        }
        return true;
    }
}
