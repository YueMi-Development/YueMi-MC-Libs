package org.yuemi.libs.api.util;

import org.jetbrains.annotations.NotNull;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtils {

    private static final Pattern NUMBER_SUFFIX_PATTERN = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)\\s*([a-zA-Z]*)$");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private NumberUtils() {}

    /**
     * Parses a string containing a number with optional suffix (e.g. 1k, 1.9k, 1m) into a double.
     *
     * @param input the input string
     * @return the parsed double value
     * @throws IllegalArgumentException if the format is invalid
     */
    public static double parseSuffix(@NotNull String input) {
        String clean = input.trim().toLowerCase();
        if (clean.isEmpty()) {
            throw new IllegalArgumentException("Empty number string");
        }

        Matcher matcher = NUMBER_SUFFIX_PATTERN.matcher(clean);
        if (!matcher.matches()) {
            try {
                return Double.parseDouble(clean);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number format: " + input);
            }
        }

        double val = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);

        if (suffix.isEmpty()) {
            return val;
        }

        switch (suffix) {
            case "k":
                return val * 1_000.0;
            case "m":
                return val * 1_000_000.0;
            case "b":
                return val * 1_000_000_000.0;
            case "t":
                return val * 1_000_000_000_000.0;
            default:
                throw new IllegalArgumentException("Unknown number suffix: " + suffix);
        }
    }

    /**
     * Formats a double into a string with a suffix (e.g. 1.9k, 1m).
     *
     * @param value the double value
     * @return the formatted string
     */
    @NotNull
    public static String formatSuffix(double value) {
        double abs = Math.abs(value);
        if (abs < 1_000.0) {
            return DECIMAL_FORMAT.format(value);
        } else if (abs < 1_000_000.0) {
            return DECIMAL_FORMAT.format(value / 1_000.0) + "k";
        } else if (abs < 1_000_000_000.0) {
            return DECIMAL_FORMAT.format(value / 1_000_000.0) + "m";
        } else if (abs < 1_000_000_000_000.0) {
            return DECIMAL_FORMAT.format(value / 1_000_000_000.0) + "b";
        } else {
            return DECIMAL_FORMAT.format(value / 1_000_000_000_000.0) + "t";
        }
    }

    /**
     * Formats a double into a string depending on the FormatType.
     *
     * @param value the value to format
     * @param type the format type (FORMATTED or RAW)
     * @return the formatted string
     */
    @NotNull
    public static String format(double value, @NotNull FormatType type) {
        if (type == FormatType.FORMATTED) {
            return formatSuffix(value);
        }
        return String.valueOf((long) value);
    }
}
