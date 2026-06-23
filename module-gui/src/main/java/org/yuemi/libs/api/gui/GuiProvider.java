package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Service provider helper for GuiApi.
 */
public final class GuiProvider {

    private static GuiApi api;

    private GuiProvider() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the active GuiApi implementation.
     *
     * @return the active GuiApi, or null if not registered
     */
    public static @Nullable GuiApi getApi() {
        return api;
    }

    /**
     * Registers the active GuiApi implementation.
     *
     * @param provider the active provider
     */
    @ApiStatus.Internal
    public static void register(@Nullable GuiApi provider) {
        api = provider;
    }
}
