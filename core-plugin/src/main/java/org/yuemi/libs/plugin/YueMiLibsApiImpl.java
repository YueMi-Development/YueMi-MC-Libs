package org.yuemi.libs.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.economy.EconomyApi;
import org.yuemi.libs.plugin.economy.EconomyApiImpl;
import org.yuemi.libs.api.items.ItemsApi;
import org.yuemi.libs.plugin.items.ItemsApiImpl;
import org.yuemi.libs.api.gui.GuiApi;
import org.yuemi.libs.plugin.gui.GuiApiImpl;
import org.yuemi.libs.api.event.EventApi;
import org.yuemi.libs.plugin.event.EventApiImpl;

final class YueMiLibsApiImpl implements YueMiLibsApi {

    private final String version;
    private final EconomyApiImpl economy = new EconomyApiImpl();
    private final ItemsApiImpl items = new ItemsApiImpl();
    private final GuiApiImpl gui = new GuiApiImpl();
    private final EventApiImpl events;

    public YueMiLibsApiImpl(@NotNull YueMiLibsPlugin plugin, @NotNull String version) {
        this.version = version;
        this.events = new EventApiImpl(plugin);
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public @NotNull EconomyApi getEconomy() {
        return economy;
    }

    EconomyApiImpl getEconomyImpl() {
        return economy;
    }

    @Override
    public @NotNull ItemsApi getItems() {
        return items;
    }

    public ItemsApiImpl getItemsImpl() {
        return items;
    }

    @Override
    public @NotNull GuiApi getGui() {
        return gui;
    }

    @Override
    public @NotNull EventApi getEvents() {
        return events;
    }

    public EventApiImpl getEventsImpl() {
        return events;
    }

    @Override
    public boolean isCompatible(@Nullable String minVersion, @Nullable String maxVersion) {
        if (minVersion != null) {
            if (compareVersions(version, minVersion) < 0) {
                return false;
            }
        }
        if (maxVersion != null) {
            if (compareVersions(version, maxVersion) > 0) {
                return false;
            }
        }
        return true;
    }

    private static int compareVersions(@NotNull String v1, @NotNull String v2) {
        String[] parts1 = v1.split("[.-]");
        String[] parts2 = v2.split("[.-]");
        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int p1 = i < parts1.length ? parseOrZero(parts1[i]) : 0;
            int p2 = i < parts2.length ? parseOrZero(parts2[i]) : 0;
            if (p1 != p2) {
                return Integer.compare(p1, p2);
            }
        }
        return 0;
    }

    private static int parseOrZero(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
