package org.yuemi.libs.api.gui;

import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * Fluent builder for creating Gui instances.
 */
public interface GuiBuilder {

    /**
     * Sets the title of the GUI.
     *
     * @param title the title
     * @return this builder
     */
    @NotNull
    GuiBuilder title(@NotNull String title);

    /**
     * Sets the number of rows of the GUI.
     *
     * @param rows the number of rows (1 to 6)
     * @return this builder
     */
    @NotNull
    GuiBuilder rows(int rows);

    /**
     * Adds an already built layout layer to the GUI.
     *
     * @param layer the layer
     * @return this builder
     */
    @NotNull
    GuiBuilder addLayer(@NotNull GuiLayer layer);

    /**
     * Inline constructs and configures a layer inside the GUI builder.
     *
     * @param id the unique ID of the layer
     * @param priority the priority of the layer
     * @param configurator lambda consumer to configure slots in this layer
     * @return this builder
     */
    @NotNull
    GuiBuilder createLayer(@NotNull String id, int priority, @NotNull Consumer<GuiLayerBuilder> configurator);

    /**
     * Builds and returns the final Gui instance.
     *
     * @return the constructed Gui
     */
    @NotNull
    Gui build();
}
