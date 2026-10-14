package org.yuemi.libs.api.platform;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlatformTest {

    @Test
    public void testPlatformEnumValues() {
        Platform[] values = Platform.values();
        assertEquals(2, values.length);
        assertNotNull(Platform.valueOf("PAPER"));
        assertNotNull(Platform.valueOf("FOLIA"));
    }

    @Test
    public void testPlatformName() {
        assertEquals("PAPER", Platform.PAPER.name());
        assertEquals("FOLIA", Platform.FOLIA.name());
    }
}
