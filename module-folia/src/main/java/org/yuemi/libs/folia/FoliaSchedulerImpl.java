package org.yuemi.libs.folia;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yuemi.libs.api.folia.FoliaScheduler;
import org.yuemi.libs.api.platform.PlatformProvider;

import java.util.concurrent.TimeUnit;

/**
 * Cross-platform scheduler implementation.
 * Uses Folia schedulers when on Folia, falls back to Bukkit schedulers on Paper.
 */
public final class FoliaSchedulerImpl implements FoliaScheduler {

    private final Server server;

    public FoliaSchedulerImpl(@NotNull Server server) {
        this.server = server;
    }

    @Override
    @NotNull
    public Server getServer() {
        return server;
    }

    @Override
    public void executeGlobal(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        if (PlatformProvider.isFolia()) {
            server.getGlobalRegionScheduler().execute(plugin, task);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    @Override
    public void runGlobalDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        if (PlatformProvider.isFolia()) {
            server.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    @Override
    public @Nullable Object runGlobalTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks, long periodTicks) {
        if (PlatformProvider.isFolia()) {
            return server.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delayTicks, periodTicks);
        } else {
            return Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks);
        }
    }

    @Override
    public void executeRegion(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
        if (PlatformProvider.isFolia()) {
            server.getRegionScheduler().execute(plugin, location, task);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    @Override
    public void runRegionDelayed(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks) {
        if (PlatformProvider.isFolia()) {
            server.getRegionScheduler().runDelayed(plugin, location, scheduledTask -> task.run(), delayTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    @Override
    public void executeAsync(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        if (PlatformProvider.isFolia()) {
            server.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    @Override
    public void runAsyncDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        if (PlatformProvider.isFolia()) {
            server.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), delayTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks);
        }
    }

    @Override
    public void runAsyncTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        if (PlatformProvider.isFolia()) {
            server.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), initialDelayTicks * 50, periodTicks * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, initialDelayTicks, periodTicks);
        }
    }

    @Override
    public void executeEntity(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task) {
        if (PlatformProvider.isFolia()) {
            entity.getScheduler().execute(plugin, task, () -> {}, 0L);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    @Override
    public void runEntityDelayed(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task, long delayTicks, long timeoutTicks) {
        if (PlatformProvider.isFolia()) {
            entity.getScheduler().runDelayed(plugin, scheduledTask -> task.run(), () -> {}, timeoutTicks);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    @Override
    public void cancelAll(@NotNull JavaPlugin plugin) {
        if (PlatformProvider.isFolia()) {
            server.getGlobalRegionScheduler().cancelTasks(plugin);
            server.getAsyncScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

}
