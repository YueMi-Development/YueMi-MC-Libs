package org.yuemi.libs.plugin;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.economy.EconomyApi;
import org.yuemi.libs.plugin.economy.EconomyApiImpl;
import org.yuemi.libs.plugin.economy.VaultEconomyProvider;

final class YueMiLibsApiImpl implements YueMiLibsApi {

    private final EconomyApiImpl economy = new EconomyApiImpl();

    @Override
    public @NotNull EconomyApi getEconomy() {
        return economy;
    }

    EconomyApiImpl getEconomyImpl() {
        return economy;
    }
}
