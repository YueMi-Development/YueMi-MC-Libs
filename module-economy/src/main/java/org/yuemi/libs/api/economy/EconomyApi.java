package org.yuemi.libs.api.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

/**
 * Manages the active economy provider and allows registering custom economy backends.
 */
public interface EconomyApi {

    /**
     * Gets the currently active economy provider, if any.
     *
     * @return the active provider, or null if no providers are available
     */
    @Nullable
    EconomyProvider getActiveProvider();

    /**
     * Registers a custom economy provider.
     *
     * @param provider the provider to register
     */
    void registerProvider(@NotNull EconomyProvider provider);

    /**
     * Sets the active economy provider by name.
     *
     * @param name the name of the provider
     * @return true if the active provider was successfully changed, false otherwise
     */
    boolean setActiveProvider(@NotNull String name);

    /**
     * Gets all registered economy providers.
     *
     * @return a collection of all registered providers
     */
    @NotNull
    Collection<EconomyProvider> getProviders();
}
