package org.yuemi.libs.api.general;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.general.compare.CompareApi;
import org.yuemi.libs.api.general.format.FormatApi;

/**
 * General utility API combining compare and format utilities.
 */
public interface GeneralApi {

    /**
     * Gets the compare utility API.
     *
     * @return the compare API
     */
    @NotNull
    CompareApi getCompare();

    /**
     * Gets the format utility API.
     *
     * @return the format API
     */
    @NotNull
    FormatApi getFormat();
}
