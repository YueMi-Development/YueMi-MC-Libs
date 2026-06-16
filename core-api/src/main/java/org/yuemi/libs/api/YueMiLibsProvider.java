package org.yuemi.libs.api;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to access YueMiLibsApi.
 */
public final class YueMiLibsProvider {

    private YueMiLibsProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the active YueMiLibsApi instance from ServicesManager.
     *
     * @return the active API instance, or null if not registered
     */
    public static @Nullable YueMiLibsApi getApi() {
        var registration = Bukkit.getServicesManager().getRegistration(YueMiLibsApi.class);
        return registration != null ? registration.getProvider() : null;
    }
}
