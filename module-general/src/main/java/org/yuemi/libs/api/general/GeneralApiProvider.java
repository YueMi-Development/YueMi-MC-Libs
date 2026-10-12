package org.yuemi.libs.api.general;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.general.compare.CompareApi;
import org.yuemi.libs.api.general.format.FormatApi;

/**
 * Provider for the GeneralApi instance.
 */
public final class GeneralApiProvider {

    private static GeneralApi instance;

    private GeneralApiProvider() {}

    /**
     * Registers the GeneralApi instance.
     *
     * @param api the API instance to register
     */
    public static void register(@Nullable GeneralApi api) {
        instance = api;
    }

    /**
     * Gets the registered GeneralApi instance.
     *
     * @return the GeneralApi instance, or null if not registered
     */
    @Nullable
    public static GeneralApi get() {
        return instance;
    }

    /**
     * Gets the registered GeneralApi instance, throwing if not available.
     *
     * @return the GeneralApi instance
     * @throws IllegalStateException if no GeneralApi is registered
     */
    @NotNull
    public static GeneralApi getRequired() {
        GeneralApi api = instance;
        if (api == null) {
            throw new IllegalStateException("GeneralApi is not registered");
        }
        return api;
    }
}
