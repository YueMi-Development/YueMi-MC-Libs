package org.yuemi.libs.api.folia;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Static accessor for the {@link FoliaScheduler}.
 * The actual implementation is provided by {@code module-folia}.
 *
 * <p>This is the entry point consumers use to schedule work that must run on both
 * Paper and Folia. On Paper the underlying schedulers are shimmed, so a single code
 * path is correct on both platforms.
 */
public final class FoliaSchedulers {

    private static volatile FoliaScheduler instance;

    private FoliaSchedulers() {
    }

    /**
     * Sets the scheduler instance.
     * Called by the plugin on enable.
     *
     * @param scheduler the scheduler
     */
    @ApiStatus.Internal
    public static void set(@NotNull FoliaScheduler scheduler) {
        instance = scheduler;
    }

    /**
     * Returns the current scheduler.
     *
     * @return the scheduler
     * @throws IllegalStateException if not initialized
     */
    @NotNull
    public static FoliaScheduler get() {
        FoliaScheduler result = instance;
        if (result == null) {
            throw new IllegalStateException("FoliaSchedulers not initialized. Call FoliaSchedulers.set() first.");
        }
        return result;
    }

    /**
     * Shortcut for {@code get().executeGlobal(plugin, task)}.
     */
    public static void executeGlobal(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        get().executeGlobal(plugin, task);
    }

    /**
     * Shortcut for {@code get().runGlobalDelayed(plugin, task, delay)}.
     */
    public static void runGlobalDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        get().runGlobalDelayed(plugin, task, delayTicks);
    }

    /**
     * Shortcut for {@code get().runGlobalTimer(plugin, task, delay, period)}.
     */
    public static void runGlobalTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks, long periodTicks) {
        get().runGlobalTimer(plugin, task, delayTicks, periodTicks);
    }

    /**
     * Shortcut for {@code get().executeRegion(plugin, location, task)}.
     */
    public static void executeRegion(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task) {
        get().executeRegion(plugin, location, task);
    }

    /**
     * Shortcut for {@code get().runRegionDelayed(plugin, location, task, delay)}.
     */
    public static void runRegionDelayed(@NotNull JavaPlugin plugin, @NotNull Location location, @NotNull Runnable task, long delayTicks) {
        get().runRegionDelayed(plugin, location, task, delayTicks);
    }

    /**
     * Shortcut for {@code get().executeEntity(plugin, entity, task)}.
     */
    public static void executeEntity(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task) {
        get().executeEntity(plugin, entity, task);
    }

    /**
     * Shortcut for {@code get().runEntityDelayed(plugin, entity, task, delay, timeout)}.
     */
    public static void runEntityDelayed(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task, long delayTicks, long timeoutTicks) {
        get().runEntityDelayed(plugin, entity, task, delayTicks, timeoutTicks);
    }

    /**
     * Shortcut for {@code get().runEntityTimer(plugin, entity, task, delay, period)}.
     *
     * @return the scheduled task handle, or null if the entity is already retired
     */
    public static @Nullable Object runEntityTimer(@NotNull JavaPlugin plugin, @NotNull Entity entity, @NotNull Runnable task, long delayTicks, long periodTicks) {
        return get().runEntityTimer(plugin, entity, task, delayTicks, periodTicks);
    }

    /**
     * Shortcut for {@code get().cancel(task)}.
     */
    public static void cancel(@Nullable Object task) {
        get().cancel(task);
    }

    /**
     * Shortcut for {@code get().executeAsync(plugin, task)}.
     */
    public static void executeAsync(@NotNull JavaPlugin plugin, @NotNull Runnable task) {
        get().executeAsync(plugin, task);
    }

    /**
     * Shortcut for {@code get().runAsyncDelayed(plugin, task, delay)}.
     */
    public static void runAsyncDelayed(@NotNull JavaPlugin plugin, @NotNull Runnable task, long delayTicks) {
        get().runAsyncDelayed(plugin, task, delayTicks);
    }

    /**
     * Shortcut for {@code get().runAsyncTimer(plugin, task, initialDelay, period)}.
     */
    public static void runAsyncTimer(@NotNull JavaPlugin plugin, @NotNull Runnable task, long initialDelayTicks, long periodTicks) {
        get().runAsyncTimer(plugin, task, initialDelayTicks, periodTicks);
    }

}
