package org.yuemi.libs.api;

import org.jetbrains.annotations.NotNull;

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
    org.yuemi.libs.api.economy.EconomyApi getEconomy();

    /**
     * Gets the items module API.
     *
     * @return the items API manager
     */
    @NotNull
    org.yuemi.libs.api.items.ItemsApi getItems();

    /**
     * Gets the GUI module API.
     *
     * @return the GUI API manager
     */
    @NotNull
    org.yuemi.libs.api.gui.GuiApi getGui();
}
