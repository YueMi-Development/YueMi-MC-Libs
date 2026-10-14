package org.yuemi.libs.api.platform;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Static accessor for the platform detector.
 * The actual implementation is provided by {@code module-folia}.
 */
@ApiStatus.Internal
public final class PlatformProvider {

    private static volatile PlatformDetector instance;

    private PlatformProvider() {
    }

    /**
     * Sets the platform detector instance.
     * Called by the plugin on enable.
     *
     * @param detector the platform detector
     */
    public static void set(@NotNull PlatformDetector detector) {
        instance = detector;
    }

    /**
     * Returns the current platform detector.
     *
     * @return the platform detector
     * @throws IllegalStateException if not initialized
     */
    @NotNull
    public static PlatformDetector get() {
        PlatformDetector result = instance;
        if (result == null) {
            throw new IllegalStateException("PlatformProvider not initialized. Call PlatformProvider.set() first.");
        }
        return result;
    }

    /**
     * Returns the current platform.
     *
     * @return {@link Platform#PAPER} or {@link Platform#FOLIA}
     */
    @NotNull
    public static Platform platform() {
        return get().getPlatform();
    }

    /**
     * Checks if running on Folia.
     *
     * @return true if Folia
     */
    public static boolean isFolia() {
        return get().isFolia();
    }

    /**
     * Checks if running on Paper.
     *
     * @return true if Paper
     */
    public static boolean isPaper() {
        return get().isPaper();
    }

}
