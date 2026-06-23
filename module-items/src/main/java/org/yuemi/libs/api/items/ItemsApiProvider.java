package org.yuemi.libs.api.items;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Service provider helper for ItemsApi.
 */
public final class ItemsApiProvider {

    private static ItemsApi api;

    private ItemsApiProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the active ItemsApi implementation.
     *
     * @return the active ItemsApi, or null if not registered
     */
    public static @Nullable ItemsApi getApi() {
        return api;
    }

    /**
     * Registers the active ItemsApi implementation.
     *
     * @param provider the active provider
     */
    @ApiStatus.Internal
    public static void register(@Nullable ItemsApi provider) {
        api = provider;
    }
}
