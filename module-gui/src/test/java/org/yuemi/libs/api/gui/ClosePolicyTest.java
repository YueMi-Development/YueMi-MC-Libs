package org.yuemi.libs.api.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClosePolicyTest {

    @Test
    public void testEnumValuesPresent() {
        assertNotNull(ClosePolicy.CLOSE);
        assertNotNull(ClosePolicy.REOPEN);
        assertNotNull(ClosePolicy.NOTHING);
    }

    @Test
    public void testEnumValueCount() {
        assertEquals(3, ClosePolicy.values().length);
    }

    @Test
    public void testEnumNames() {
        assertEquals("CLOSE", ClosePolicy.CLOSE.name());
        assertEquals("REOPEN", ClosePolicy.REOPEN.name());
        assertEquals("NOTHING", ClosePolicy.NOTHING.name());
    }
}
