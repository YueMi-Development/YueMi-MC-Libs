package org.yuemi.libs.plugin.gui;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.gui.ClosePolicy;
import org.yuemi.libs.api.gui.Gui;
import org.yuemi.libs.api.gui.GuiBuilder;
import org.yuemi.libs.api.gui.GuiLayer;
import org.yuemi.libs.api.gui.GuiLayerBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class GuiBuilderImpl implements GuiBuilder {

    private String title = "GUI";
    private int rows = 6;
    private final List<GuiLayer> layers = new ArrayList<>();
    private ClosePolicy closePolicy = ClosePolicy.CLOSE;
    private Consumer<org.bukkit.entity.Player> onClose;

    @Override
    public @NotNull GuiBuilder title(@NotNull String title) {
        this.title = title;
        return this;
    }

    @Override
    public @NotNull GuiBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }

    @Override
    public @NotNull GuiBuilder addLayer(@NotNull GuiLayer layer) {
        layers.add(layer);
        return this;
    }

    @Override
    public @NotNull GuiBuilder createLayer(@NotNull String id, int priority, @NotNull Consumer<GuiLayerBuilder> configurator) {
        GuiLayerBuilderImpl builder = new GuiLayerBuilderImpl(id, priority);
        configurator.accept(builder);
        layers.add(builder.build());
        return this;
    }

    @Override
    public @NotNull GuiBuilder closePolicy(@NotNull ClosePolicy policy) {
        this.closePolicy = policy;
        return this;
    }

    @Override
    public @NotNull GuiBuilder onClose(@NotNull Consumer<org.bukkit.entity.Player> callback) {
        this.onClose = callback;
        return this;
    }

    @Override
    public @NotNull Gui build() {
        GuiImpl gui = new GuiImpl(title, rows);
        gui.setClosePolicy(closePolicy);
        gui.setOnClose(onClose);
        for (GuiLayer layer : layers) {
            gui.addLayer(layer);
        }
        return gui;
    }
}
