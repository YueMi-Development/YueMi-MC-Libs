package org.yuemi.libs.api.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum FormatType {
    FORMATTED,
    RAW;

    @NotNull
    public static FormatType fromString(@NotNull String name) {
        try {
            return valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return RAW;
        }
    }
}
