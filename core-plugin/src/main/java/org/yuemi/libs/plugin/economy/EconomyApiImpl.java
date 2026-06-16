package org.yuemi.libs.plugin.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.economy.EconomyApi;
import org.yuemi.libs.api.economy.EconomyProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EconomyApiImpl implements EconomyApi {

    private final Map<String, EconomyProvider> providers = new ConcurrentHashMap<>();
    private @Nullable String activeProviderName;

    @Override
    public @Nullable EconomyProvider getActiveProvider() {
        if (activeProviderName != null) {
            EconomyProvider provider = providers.get(activeProviderName);
            if (provider != null && provider.isAvailable()) {
                return provider;
            }
        }
        // Fallback to the first available provider
        for (EconomyProvider provider : providers.values()) {
            if (provider.isAvailable()) {
                return provider;
            }
        }
        return null;
    }

    @Override
    public void registerProvider(@NotNull EconomyProvider provider) {
        providers.put(provider.getName(), provider);
    }

    @Override
    public boolean setActiveProvider(@NotNull String name) {
        EconomyProvider provider = providers.get(name);
        if (provider != null && provider.isAvailable()) {
            this.activeProviderName = name;
            return true;
        }
        return false;
    }

    @Override
    public @NotNull Collection<EconomyProvider> getProviders() {
        return Collections.unmodifiableCollection(providers.values());
    }
}
