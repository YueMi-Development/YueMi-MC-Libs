package org.yuemi.libs.api.util;

import org.jetbrains.annotations.NotNull;

public enum FormatType {
    FORMATTED,
    RAW;

    @NotNull
    public static FormatType fromString(@NotNull String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RAW;
        }
    }
}
