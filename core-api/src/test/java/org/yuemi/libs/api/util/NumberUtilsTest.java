package org.yuemi.libs.api.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberUtilsTest {

    @Test
    public void testFormatSuffix() {
        assertEquals("100", NumberUtils.formatSuffix(100));
        assertEquals("1k", NumberUtils.formatSuffix(1000));
        assertEquals("1.9k", NumberUtils.formatSuffix(1900));
        assertEquals("1.5m", NumberUtils.formatSuffix(1500000));
        assertEquals("2.5b", NumberUtils.formatSuffix(2500000000.0));
    }

    @Test
    public void testParseSuffix() {
        assertEquals(100.0, NumberUtils.parseSuffix("100"));
        assertEquals(1000.0, NumberUtils.parseSuffix("1k"));
        assertEquals(1900.0, NumberUtils.parseSuffix("1.9k"));
        assertEquals(1500000.0, NumberUtils.parseSuffix("1.5m"));
        assertEquals(2500000000.0, NumberUtils.parseSuffix("2.5b"));
        assertEquals(1900.0, NumberUtils.parseSuffix(" 1.9K "));
    }

    @Test
    public void testFormatWithFormatType() {
        assertEquals("1.9k", NumberUtils.format(1900, FormatType.FORMATTED));
        assertEquals("1900", NumberUtils.format(1900, FormatType.RAW));
    }

    @Test
    public void testInvalidParse() {
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.parseSuffix("abc"));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.parseSuffix("1.2.3k"));
    }
}
