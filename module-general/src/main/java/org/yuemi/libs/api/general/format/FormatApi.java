package org.yuemi.libs.api.general.format;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility API for formatting numbers.
 */
public class FormatApi {

    private static final FormatApi INSTANCE = new FormatApi();

    public static FormatApi getInstance() {
        return INSTANCE;
    }

    private final NumberFormat numberFormat;
    private final DecimalFormat decimalFormat;

    private FormatApi() {
        this.numberFormat = NumberFormat.getNumberInstance(Locale.US);
        this.decimalFormat = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
    }

    /**
     * Formats a number with thousand separators.
     *
     * @param number the number to format
     * @return formatted string (e.g., "1,234,567")
     */
    @NotNull
    public String formatNumber(long number) {
        return numberFormat.format(number);
    }

    /**
     * Formats a double with thousand separators and 2 decimal places.
     *
     * @param number the number to format
     * @return formatted string (e.g., "1,234,567.89")
     */
    @NotNull
    public String formatDecimal(double number) {
        return decimalFormat.format(number);
    }

    /**
     * Formats a number with shorthand notation for large numbers.
     *
     * @param number the number to format
     * @return formatted string (e.g., "1.2K", "3.4M", "5.6B")
     */
    @NotNull
    public String formatCompact(long number) {
        if (number >= 1_000_000_000_000L) {
            return String.format(Locale.US, "%.1fT", number / 1_000_000_000_000.0).replaceAll("\\.0([A-Z])$", "$1");
        } else if (number >= 1_000_000_000L) {
            return String.format(Locale.US, "%.1fB", number / 1_000_000_000.0).replaceAll("\\.0([A-Z])$", "$1");
        } else if (number >= 1_000_000L) {
            return String.format(Locale.US, "%.1fM", number / 1_000_000.0).replaceAll("\\.0([A-Z])$", "$1");
        } else if (number >= 1_000L) {
            return String.format(Locale.US, "%.1fK", number / 1_000.0).replaceAll("\\.0([A-Z])$", "$1");
        } else {
            return numberFormat.format(number);
        }
    }

    /**
     * Formats duration in seconds to human-readable string.
     *
     * @param seconds duration in seconds
     * @return formatted string (e.g., "2h 30m", "45s")
     */
    @NotNull
    public String formatDuration(long seconds) {
        if (seconds >= 86400) {
            long days = seconds / 86400;
            long remainingHours = (seconds % 86400) / 3600;
            return remainingHours > 0 ? days + "d " + remainingHours + "h" : days + "d";
        } else if (seconds >= 3600) {
            long hours = seconds / 3600;
            long remainingMinutes = (seconds % 3600) / 60;
            return remainingMinutes > 0 ? hours + "h " + remainingMinutes + "m" : hours + "h";
        } else if (seconds >= 60) {
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return remainingSeconds > 0 ? minutes + "m " + remainingSeconds + "s" : minutes + "m";
        } else {
            return seconds + "s";
        }
    }

    /**
     * Formats percentage.
     *
     * @param value the value (0.0 to 1.0)
     * @return formatted percentage string (e.g., "75%")
     */
    @NotNull
    public String formatPercent(double value) {
        return String.format(Locale.US, "%.1f%%", value * 100);
    }
}
