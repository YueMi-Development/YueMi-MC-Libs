package org.yuemi.libs.impl.platform;

import io.papermc.paper.ServerBuildInfo;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.platform.Platform;
import org.yuemi.libs.api.platform.PlatformDetector;

/**
 * Platform detector implementation using Paper's ServerBuildInfo API.
 */
public final class PlatformDetectorImpl implements PlatformDetector {

    private final Platform platform;

    public PlatformDetectorImpl() {
        this.platform = detectPlatform();
    }

    private Platform detectPlatform() {
        try {
            ServerBuildInfo buildInfo = ServerBuildInfo.buildInfo();
            if (buildInfo != null && buildInfo.isBrandCompatible(Key.key("papermc", "folia"))) {
                return Platform.FOLIA;
            }
        } catch (Throwable ignored) {
        }

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return Platform.FOLIA;
        } catch (ClassNotFoundException e) {
            return Platform.PAPER;
        }
    }

    @Override
    @NotNull
    public Platform getPlatform() {
        return platform;
    }

    @Override
    public boolean isFolia() {
        return platform == Platform.FOLIA;
    }

    @Override
    public boolean isPaper() {
        return platform == Platform.PAPER;
    }

}
