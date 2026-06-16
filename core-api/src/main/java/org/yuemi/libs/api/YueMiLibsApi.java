package org.yuemi.libs.api;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.economy.EconomyApi;

/**
 * Public API for YueMi Libs plugin.
 */
public interface YueMiLibsApi {

    /**
     * Gets the economy module API.
     *
     * @return the economy API manager
     */
    @NotNull
    EconomyApi getEconomy();
}
