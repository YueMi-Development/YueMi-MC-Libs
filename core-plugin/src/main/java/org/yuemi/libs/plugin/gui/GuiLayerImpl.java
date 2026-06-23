package org.yuemi.libs.plugin.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.gui.GuiItem;
import org.yuemi.libs.api.gui.GuiLayer;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GuiLayerImpl implements GuiLayer {

    private final String id;
    private final int priority;
    private final Map<Integer, GuiItem> items = new ConcurrentHashMap<>();

    public GuiLayerImpl(@NotNull String id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setItem(int slot, @Nullable GuiItem item) {
        if (item == null) {
            items.remove(slot);
        } else {
            items.put(slot, item);
        }
    }

    @Override
    public @Nullable GuiItem getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public void removeItem(int slot) {
        items.remove(slot);
    }

    @Override
    public @NotNull Map<Integer, GuiItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
