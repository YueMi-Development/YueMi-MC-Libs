package org.yuemi.libs.api.platform;

import org.jetbrains.annotations.NotNull;

/**
 * Provides platform detection for Paper vs Folia.
 */
public interface PlatformDetector {

    /**
     * Returns the current server platform.
     *
     * @return {@link Platform#PAPER} or {@link Platform#FOLIA}
     */
    @NotNull Platform getPlatform();

    /**
     * Checks if the current platform is Folia.
     *
     * @return true if running on Folia
     */
    boolean isFolia();

    /**
     * Checks if the current platform is Paper (or Spigot).
     *
     * @return true if running on Paper or compatible
     */
    boolean isPaper();

}
