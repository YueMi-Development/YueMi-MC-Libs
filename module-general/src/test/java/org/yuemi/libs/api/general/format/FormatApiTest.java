package org.yuemi.libs.api.general.format;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FormatApiTest {

    private final FormatApi api = FormatApi.getInstance();

    @Test
    public void testFormatNumber() {
        assertEquals("1,234", api.formatNumber(1234));
        assertEquals("1,234,567", api.formatNumber(1234567));
        assertEquals("0", api.formatNumber(0));
        assertEquals("100", api.formatNumber(100));
    }

    @Test
    public void testFormatDecimal() {
        assertEquals("1,234.57", api.formatDecimal(1234.567));
        assertEquals("0.00", api.formatDecimal(0));
        assertEquals("100.00", api.formatDecimal(100));
        assertEquals("1,000.23", api.formatDecimal(1000.23));
    }

    @Test
    public void testFormatCompact() {
        assertEquals("0", api.formatCompact(0));
        assertEquals("100", api.formatCompact(100));
        assertEquals("1K", api.formatCompact(1000));
        assertEquals("1.5K", api.formatCompact(1500));
        assertEquals("10K", api.formatCompact(10000));
        assertEquals("1M", api.formatCompact(1000000));
        assertEquals("1.5M", api.formatCompact(1500000));
        assertEquals("100M", api.formatCompact(100000000));
        assertEquals("1B", api.formatCompact(1000000000));
        assertEquals("1.5B", api.formatCompact(1500000000));
        assertEquals("1T", api.formatCompact(1000000000000L));
        assertEquals("1.5T", api.formatCompact(1500000000000L));
    }

    @Test
    public void testFormatDurationSeconds() {
        assertEquals("0s", api.formatDuration(0));
        assertEquals("45s", api.formatDuration(45));
        assertEquals("59s", api.formatDuration(59));
    }

    @Test
    public void testFormatDurationMinutes() {
        assertEquals("1m", api.formatDuration(60));
        assertEquals("1m 30s", api.formatDuration(90));
        assertEquals("5m", api.formatDuration(300));
        assertEquals("5m 45s", api.formatDuration(345));
    }

    @Test
    public void testFormatDurationHours() {
        assertEquals("1h", api.formatDuration(3600));
        assertEquals("1h 30m", api.formatDuration(5400));
        assertEquals("2h", api.formatDuration(7200));
        assertEquals("2h 15m", api.formatDuration(8100));
    }

    @Test
    public void testFormatDurationDays() {
        assertEquals("1d", api.formatDuration(86400));
        assertEquals("1d 5h", api.formatDuration(104400)); // 1 day + 5 hours + 40 min + 0 sec
        assertEquals("2d", api.formatDuration(172800));
        assertEquals("3d 12h", api.formatDuration(302400)); // 3 days + 12 hours
    }

    @Test
    public void testFormatPercent() {
        assertEquals("0.0%", api.formatPercent(0.0));
        assertEquals("100.0%", api.formatPercent(1.0));
        assertEquals("50.0%", api.formatPercent(0.5));
        assertEquals("75.5%", api.formatPercent(0.755));
        assertEquals("33.3%", api.formatPercent(1.0 / 3.0));
    }
}
