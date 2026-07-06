package org.yuemi.libs.plugin.listeners;

import com.nexomc.nexo.api.events.NexoItemsLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.yuemi.libs.plugin.YueMiLibsPlugin;

/**
 * Listener that ensures Nexo items are only accessed after Nexo has finished loading.
 */
public final class NexoItemsLoadedListener implements Listener {

    private final YueMiLibsPlugin plugin;

    public NexoItemsLoadedListener(YueMiLibsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNexoItemsLoaded(NexoItemsLoadedEvent event) {
        plugin.getLogger().info("Nexo items loaded – providers can now be safely used.");
    }
}
