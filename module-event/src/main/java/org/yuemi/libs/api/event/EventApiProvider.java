package org.yuemi.libs.api.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Service provider helper for EventApi.
 */
public final class EventApiProvider {

    private static EventApi api;

    private EventApiProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the active EventApi implementation.
     *
     * @return the active EventApi, or null if not registered
     */
    public static @Nullable EventApi getApi() {
        return api;
    }

    /**
     * Registers the active EventApi implementation.
     *
     * @param provider the active provider
     */
    @ApiStatus.Internal
    public static void register(@Nullable EventApi provider) {
        api = provider;
    }
}
