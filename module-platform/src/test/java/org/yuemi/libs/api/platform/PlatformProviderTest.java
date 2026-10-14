package org.yuemi.libs.api.platform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlatformProviderTest {

    @Test
    public void testPlatformProviderIsNotInitialized() {
        // PlatformProvider should throw if not initialized
        assertThrows(IllegalStateException.class, PlatformProvider::get);
    }

    @Test
    public void testStaticHelpersThrowWhenUninitialized() {
        assertThrows(IllegalStateException.class, PlatformProvider::platform);
        assertThrows(IllegalStateException.class, PlatformProvider::isFolia);
        assertThrows(IllegalStateException.class, PlatformProvider::isPaper);
    }
}
