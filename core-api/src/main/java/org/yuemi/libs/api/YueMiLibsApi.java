package org.yuemi.libs.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    /**
     * Gets the version of the YueMi Libs plugin.
     *
     * @return the version string
     */
    @NotNull
    String getVersion();

    /**
     * Checks if the current version of YueMi Libs is compatible with the requested range.
     *
     * @param minVersion the minimum required version (inclusive), or null if no minimum limit
     * @param maxVersion the maximum required version (inclusive), or null if no maximum limit
     * @return true if compatible, false otherwise
     */
    boolean isCompatible(@Nullable String minVersion, @Nullable String maxVersion);
}
