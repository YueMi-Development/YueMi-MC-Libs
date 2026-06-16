package org.yuemi.libs.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.libs.api.YueMiLibsApi;

public final class YueMiLibsPlugin extends JavaPlugin {

    private YueMiLibsApi api;

    @Override
    public void onEnable() {
        this.api = new YueMiLibsApiImpl();

        getServer().getServicesManager().register(
                YueMiLibsApi.class,
                api,
                this,
                ServicePriority.Normal
        );

        getLogger().info("YueMi Libs has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (api != null) {
            getServer().getServicesManager().unregister(YueMiLibsApi.class, api);
        }
        getLogger().info("YueMi Libs has been disabled.");
    }
}
