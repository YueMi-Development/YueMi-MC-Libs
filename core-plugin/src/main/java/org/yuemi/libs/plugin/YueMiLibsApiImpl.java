package org.yuemi.libs.plugin;

import org.jetbrains.annotations.NotNull;
import org.yuemi.libs.api.YueMiLibsApi;
import org.yuemi.libs.api.economy.EconomyApi;
import org.yuemi.libs.plugin.economy.EconomyApiImpl;
import org.yuemi.libs.api.items.ItemsApi;
import org.yuemi.libs.plugin.items.ItemsApiImpl;
import org.yuemi.libs.api.gui.GuiApi;
import org.yuemi.libs.plugin.gui.GuiApiImpl;

final class YueMiLibsApiImpl implements YueMiLibsApi {

    private final EconomyApiImpl economy = new EconomyApiImpl();
    private final ItemsApiImpl items = new ItemsApiImpl();
    private final GuiApiImpl gui = new GuiApiImpl();

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
}
